/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.service;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import com.demai.cornel.config.ServiceMobileConfig;
import com.demai.cornel.dao.CommodityDao;
import com.demai.cornel.dao.DryTowerDao;
import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.dmEnum.IdTypeEnum;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.model.DryTower;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.purcharse.dao.CompanyInfoMapper;
import com.demai.cornel.purcharse.dao.LocationInfoMapper;
import com.demai.cornel.purcharse.model.CompanyInfo;
import com.demai.cornel.purcharse.model.LocationInfo;
import com.demai.cornel.util.*;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.delivery.GetDriverCornInfoResp;
import com.demai.cornel.vo.delivery.GetSupplierCornInfoResp;
import com.demai.cornel.vo.supplier.*;
import com.demai.cornel.vo.tower.TowerOperaResp;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.vo.delivery.DeliveryTaskListResp;
import com.demai.cornel.vo.order.GetOrderInfoReq;
import com.demai.cornel.vo.order.OperationOrderReq;
import com.demai.cornel.vo.order.OperationOrderResp;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.demai.cornel.vo.task.GetOrderListResp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * Create By tfzhu 2019/12/19 8:08 AM 烘干塔服务接口
 */
@Service @Slf4j public class SupplierTaskService {

    @Resource private OrderInfoDao orderInfoDao;

    @Resource private DryTowerDao dryTowerDao;

    @Resource private UserInfoDao userInfoDao;
    @Resource private CommodityDao commodityDao;
    @Resource private ImgService imgService;
    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private CompanyInfoMapper companyInfoMapper;

    /**
     * 根据用户烘干塔用户id 订单状态查询任务订单
     *
     * @param supplierId
     * @param param
     */
    public Collection<SupplierTaskListResp> getTaskOrderListByStatus(String supplierId, GetOrderListReq param) {
        List<GetOrderListResp> orderListResp = orderInfoDao
                .getOrderInfoBySupplier(supplierId, param.getOrderType(), param.getOrderId(), param.getPgSize());
        if (CollectionUtils.isEmpty(orderListResp)) {
            log.info("supplier query order list is empty towerId:{} param:{}", supplierId, JsonUtil.toJson(param));
            return null;
        }
        Map<String, SupplierTaskListResp> taskOrderInfo = Maps.newHashMap();
        orderListResp.stream().forEach(order -> {
            if (taskOrderInfo.keySet().contains(order.getTaskId())) {
                buildTaskOrderInfo(taskOrderInfo.get(order.getTaskId()), order);
            } else {
                taskOrderInfo.put(order.getTaskId(), buildTaskOrderInfo(null, order));
            }
        });
        return taskOrderInfo.values();
    }

    /**
     * 根据用户烘干塔用户id 订单状态查询任务订单
     *
     * @param supplierId
     * @param param
     */
    public List<GetOrderListResp> getTaskOrderListByStatusV2(String supplierId, GetOrderListReq param) {
        List<GetOrderListResp> orderListResp = orderInfoDao
                .getOrderInfoBySupplier(supplierId, param.getOrderType(), param.getOrderId(), param.getPgSize());
        if (CollectionUtils.isEmpty(orderListResp)) {
            log.info("supplier query order list is empty towerId:{} param:{}", supplierId, JsonUtil.toJson(param));
            return Lists.newArrayList();
        }
        orderListResp.stream().forEach(order -> {
            order.setOrderStatusDesc(GetOrderListResp.STATUS_DESC_ENUE.NORMAL.getValue());
            if (order.getOrderStatus().compareTo(OrderInfo.STATUS_ENUE.ORDER_INIT.getValue()) == 0 && DateUtils
                    .checkStartTimeBeforeNow(order.getStartTime())) {
                order.setOrderStatusDesc(GetOrderListResp.STATUS_DESC_ENUE.DELAY.getValue());
            }
            if (CollectionUtils.isNotEmpty(order.getDriverMobileSet())) {
                order.setDriverMobile(order.getDriverMobileSet().iterator().next());
            }
        });
        return orderListResp;
    }

    /***
     * 根据查询的订单构建烘干塔返回的数据
     *
     * @param order 数据库返回的订单信息
     * @return
     */
    private SupplierTaskListResp buildTaskOrderInfo(SupplierTaskListResp resp, GetOrderListResp order) {
        DeliveryTaskListResp.DeliveryTaskOrderInfo orderInfo = new DeliveryTaskListResp.DeliveryTaskOrderInfo();
        BeanUtils.copyProperties(order, orderInfo);
        if (resp != null) {
            resp.getOrderInfo().add(orderInfo);
        } else {
            resp = new SupplierTaskListResp();
            BeanUtils.copyProperties(order, resp);
            resp.setOrderInfo(Lists.newArrayList(orderInfo));
        }
        return resp;
    }

    /**
     * 根据用户烘干塔用户id 订单状态查询任务订单
     *
     * @param supplierId
     * @param param
     */
    public GetOrderListResp getTaskOrderInfoByOrderIdOrVerifyCode(String supplierId, GetOrderInfoReq param) {
        GetOrderListResp result = orderInfoDao
                .getOrderInfoByOrderIdOrVerifyCode("supplier", supplierId, param.getOrderId(),
                        StringUtil.isNotEmpty(param.getVerifyCode()) ?
                                param.getVerifyCode().toUpperCase() :
                                param.getVerifyCode());
        if (result == null) {
            result = new GetOrderListResp();
        } else {
            if (result.getOrderStatus().compareTo(OrderInfo.STATUS_ENUE.ORDER_INIT.getValue()) == 0 && DateUtils
                    .checkStartTimeBeforeNow(result.getStartTime())) {
                result.setOrderStatusDesc(GetOrderListResp.STATUS_DESC_ENUE.DELAY.getValue());
            }
            if (CollectionUtils.isNotEmpty(result.getDriverMobileSet())) {
                result.setDriverMobile(result.getDriverMobileSet().iterator().next());
            }
        }
        return result;
    }

    /**
     * 烘干塔开始装货
     *
     * @param supplierId
     * @param orderId
     * @return
     */
    public OperationOrderResp shipmentStart(String supplierId, String orderId) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        Set<String> sendOutId = new HashSet<>(1);
        sendOutId.add(supplierId);
        orderInfo.setSendOutUserId(sendOutId);
        orderInfo.setStatus(OrderInfo.STATUS_ENUE.SUPPLIER_CONFIRM_SHIPMENT.getValue());
        orderInfo.setOldStatus(OrderInfo.STATUS_ENUE.ORDER_ARRIVE_DEP.getValue());
        int num = orderInfoDao.updateShipmentStatusByOldStatus(orderInfo);
        if (log.isDebugEnabled()) {
            log.debug("supplier start shipment update order num is zero");
        }
        if (num == 0) {
            return OperationOrderResp.builder().sendOutTime(DateFormatUtils.formatDateTime(new Date()))
                    .opResult(OperationOrderResp.SUPPLIER_RESP_STATUS_ENUE.OPERATION_ERROR.getValue())
                    .success(Boolean.FALSE).orderId(orderId).orderStatus(orderInfo.getStatus()).build();
        }
        return OperationOrderResp.builder().sendOutTime(DateFormatUtils.formatDateTime(new Date()))
                .success(Boolean.TRUE).opResult(OperationOrderResp.SUPPLIER_RESP_STATUS_ENUE.SUCCESS.getValue())
                .orderId(orderId).orderStatus(orderInfo.getStatus()).build();
    }

    /**
     * 司机确认装货完成  烘干塔开始录入出货信息
     *
     * @param userId
     * @param param
     * @return
     */
    public OperationOrderResp driverShipmentOver(String userId, OperationOrderReq param) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(param.getOrderId());
        orderInfo.setUserId(userId);
        orderInfo.setStatus(OrderInfo.STATUS_ENUE.ORDER_SHIPMENT.getValue());
        orderInfo.setOldStatus(OrderInfo.STATUS_ENUE.SUPPLIER_CONFIRM_SHIPMENT.getValue());
        int num = orderInfoDao.updateShipmentStatusByOldStatus(orderInfo);
        if (log.isDebugEnabled()) {
            log.debug("supplier shipment over update order num is zero");
        }
        if (num == 0) {
            return OperationOrderResp.builder().sendOutTime(DateFormatUtils.formatDateTime(new Date()))
                    .success(Boolean.FALSE)
                    .opResult(OperationOrderResp.SUPPLIER_RESP_STATUS_ENUE.OPERATION_ERROR.getValue())
                    .orderId(param.getOrderId()).orderStatus(orderInfo.getStatus()).build();
        }
        orderInfo = orderInfoDao.getOrderInfoByOrderId(param.getOrderId());
        return OperationOrderResp.builder().sendOutTime(DateFormatUtils.formatDateTime(new Date()))
                .success(Boolean.TRUE).opResult(OperationOrderResp.SUPPLIER_RESP_STATUS_ENUE.SUCCESS.getValue())
                .orderId(param.getOrderId()).realWeight(orderInfo.getCarryWeight()).orderStatus(orderInfo.getStatus())
                .build();
    }

    /**
     * 烘干塔装货完成
     *
     * @param supplierId
     * @param param
     * @return
     */
    public OperationOrderResp shipmentOver(String supplierId, OperationOrderReq param) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(param.getOrderId());
        Set<String> sendOutId = new HashSet<>(1);
        sendOutId.add(supplierId);
        orderInfo.setSendOutUserId(sendOutId);
        orderInfo.setCarryWeight(param.getRealWeight());
        orderInfo.setSendOutTime(DateFormatUtils.formatDateTime(new java.sql.Date(System.currentTimeMillis())));
        orderInfo.setStatus(OrderInfo.STATUS_ENUE.ORDER_SHIPMENT_OVER.getValue());
        orderInfo.setOldStatus(OrderInfo.STATUS_ENUE.ORDER_SHIPMENT.getValue());
        int num = orderInfoDao.updateShipmentStatusByOldStatus(orderInfo);
        if (log.isDebugEnabled()) {
            log.debug("supplier shipment over update order num is zero");
        }
        if (num == 0) {
            return OperationOrderResp.builder().sendOutTime(DateFormatUtils.formatDateTime(new Date()))
                    .success(Boolean.FALSE)
                    .opResult(OperationOrderResp.SUPPLIER_RESP_STATUS_ENUE.OPERATION_ERROR.getValue())
                    .realWeight(new BigDecimal(0.0)).orderId(param.getOrderId()).orderStatus(orderInfo.getStatus())
                    .build();
        }
        orderInfo = orderInfoDao.getOrderInfoByOrderId(param.getOrderId());
        return OperationOrderResp.builder().sendOutTime(DateFormatUtils.formatDateTime(new Date()))
                .success(Boolean.TRUE).opResult(OperationOrderResp.SUPPLIER_RESP_STATUS_ENUE.SUCCESS.getValue())
                .orderId(param.getOrderId()).realWeight(orderInfo.getCarryWeight()).orderStatus(orderInfo.getStatus())
                .build();
    }

    /**
     * 烘干塔注册 增加烘干塔
     *
     * @param dryTower
     * @return
     */
    public DryTower.REGISTER_STATUS dryTowerRegister(SupplierRegisterReq dryTower) {
        try {
            log.debug("dry tower register info is [{}]", JacksonUtils.obj2String(dryTower));
            String curUser = UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME);
            dryTower.setBindUserId(curUser);
            if (!checkDryTowerParam(dryTower)) {
                return DryTower.REGISTER_STATUS.PARAM_ERROR;
            }
            //校验联系人在不在库里面 不在则插入 在则校验一下联系人的身份证号跟名字跟请求中是否相同
            UserInfo contactUserInfo = userInfoDao.getDryTowerUserInfoByPhone(dryTower.getContactMobile());
            String contactUserId;
            if (contactUserInfo == null) {
                UserInfo insertContact = new UserInfo();
                insertContact.setUserId(UUID.randomUUID().toString());
                insertContact.setName(dryTower.getContactsName());
                insertContact.setRole(UserInfo.ROLE_ENUE.SUPPLIER.getValue());
                insertContact.setIdType(1);
                insertContact.setIdCard(dryTower.getUserIdCard());
                insertContact.setMobile(Sets.newHashSet(dryTower.getContactMobile()));
                userInfoDao.save(insertContact);
                contactUserId = insertContact.getUserId();
            } else {
                contactUserId = contactUserInfo.getUserId();
                if (!dryTower.getContactsName().equals(contactUserInfo.getName()) || !dryTower.getUserIdCard()
                        .equals(contactUserInfo.getIdCard())) {
                    log.info("dry tower register fail due to contact info error ");
                    return DryTower.REGISTER_STATUS.CONTACT_ERROR;
                }
            }

            String finalContactUserId = contactUserId;
            dryTower.getTowerInfos().stream().forEach(x -> {
                DryTower dryTowerInsert = new DryTower();
                dryTowerInsert.setTowerId(UUID.randomUUID().toString());
                BeanUtils.copyProperties(dryTower, dryTowerInsert);
                BeanUtils.copyProperties(x, dryTowerInsert);
                dryTowerInsert.setContactUserId(Sets.newHashSet(finalContactUserId, CookieAuthUtils.getCurrentUser()));
                dryTowerDao.insertSelective(dryTowerInsert);
            });

        } catch (Exception e) {
            log.error("dry tower register fail ", e);
            return DryTower.REGISTER_STATUS.SERVICE_ERROR;
        }
        return DryTower.REGISTER_STATUS.SUCCESS;
    }

    public static void main(String[] args) {
        Set<String> get = Sets.newHashSet("1", "2", "3", "1");
        System.out.println("ok");
    }

    /**
     * 校验烘干塔参数
     * todo 其他校验细节还需讨论 当前的校验是联系人跟联系方式不能为空
     *
     * @param dryTower
     * @return
     */
    private boolean checkDryTowerParam(SupplierRegisterReq dryTower) {
        if (dryTower == null) {
            return false;
        }
        if (Strings.isNullOrEmpty(dryTower.getContactsName()) || Strings.isNullOrEmpty(dryTower.getContactMobile())
                || CollectionUtils.isEmpty(dryTower.getTowerInfos())) {
            return false;
        }
        return true;
    }

    /**
     * 根据烘干塔ID 获取烘干塔信息
     *
     * @param towerId
     * @return
     */
    public TowerInfoResp getTowerInfoByTowerId(String towerId) {
        DryTower dryTower = dryTowerDao.selectByTowerId(towerId);
        TowerInfoResp getTowerInfoResp = new TowerInfoResp();
        if (dryTower == null) {
            getTowerInfoResp.setOptStatus(TowerInfoResp.REGISTER_STATUS.NO_RESULT.getValue());
            return getTowerInfoResp;
        }
        BeanUtils.copyProperties(dryTower, getTowerInfoResp);
        List<Commodity> commodities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dryTower.getCommodityId())) {
            commodities = commodityDao.getCommodityByIds(dryTower.getCommodityId());
        }
        getTowerInfoResp.setCommoditys(commodities);
        getTowerInfoResp.setOptStatus(TowerInfoResp.REGISTER_STATUS.SUCCESS.getValue());
        return getTowerInfoResp;

    }

    /**
     * 更新烘干塔信息
     *
     * @param dryTower
     * @return
     */
    public TowerOperaResp updateTowerInfo(DryTower dryTower) {
        log.info("edit tower info is [{}]", JacksonUtils.obj2String(dryTower));
        if (dryTower == null || Strings.isNullOrEmpty(dryTower.getTowerId())) {
            return TowerOperaResp.builder().status(TowerOperaResp.OPERATION_STATUS.PARAM_ERROR.getValue()).build();
        }
        if (dryTower.getDefaultFlag() != null && dryTower.getDefaultFlag().equals(1)) {
            dryTowerDao.updateTowerNonDefaultFlag(CookieAuthUtils.getCurrentUser());
        }
        if(!Strings.isNullOrEmpty(dryTower.getLocation())){
            LocationInfo locationInfo = new LocationInfo();
            locationInfo.setLocationArea(dryTower.getLocationArea());
            locationInfo.setLocationDetail(dryTower.getLocationDetail());
            locationInfo.setLocation(dryTower.getLocation());
            locationInfo.setLocationId(UUID.randomUUID().toString());
            locationInfoMapper.insertSelective(locationInfo);
            dryTower.setLocationId(locationInfo.getLocationId());
        }
        int rest = dryTowerDao.updateByPrimaryKeySelective(dryTower);
        if (rest == 0) {
            log.info("update tower info error due to update db fail tower info is [{}]",
                    JacksonUtils.obj2String(dryTower));
            return TowerOperaResp.builder().status(TowerOperaResp.OPERATION_STATUS.NO_TOWER.getValue()).build();
        }
        return TowerOperaResp.builder().status(TowerOperaResp.OPERATION_STATUS.SUCCESS.getValue())
                .towerId(dryTower.getTowerId()).build();

    }

    /**
     * 烘干塔侧点击增加烘干塔增加新的烘干塔
     * todo 联系人跟联系方式逻辑需要添加
     *
     * @param addDryTowerReq
     * @return
     */
    public TowerOperaResp adddTowerInfo(AddDryTowerReq addDryTowerReq) {
        if (addDryTowerReq == null || Strings.isNullOrEmpty(addDryTowerReq.getLocationArea()) || Strings
                .isNullOrEmpty(addDryTowerReq.getLocationDetail())) {
            log.debug("add dry tower error due tu tower info  error [{}]", JacksonUtils.obj2String(addDryTowerReq));
            return TowerOperaResp.builder().status(TowerOperaResp.OPERATION_STATUS.PARAM_ERROR.getValue()).build();
        }
        UserInfo userInfo = userInfoDao.getUserInfoByUserId(CookieAuthUtils.getCurrentUser());
        if (userInfo == null || !userInfo.getRole().equals(UserInfo.ROLE_ENUE.SUPPLIER.getValue())) {
            log.debug("add dry tower error due to user if error [{}]", JacksonUtils.obj2String(userInfo));
            return TowerOperaResp.builder().status(TowerOperaResp.OPERATION_STATUS.AUTH_ERROR.getValue()).build();
        }
        DryTower dryTower = new DryTower();
        BeanUtils.copyProperties(addDryTowerReq, dryTower);
        dryTower.setBindUserId(CookieAuthUtils.getCurrentUser());
        dryTower.setTowerId(UUID.randomUUID().toString());
        dryTower.setContactUserId(Sets.newHashSet(CookieAuthUtils.getCurrentUser()));
        if (addDryTowerReq.getDefaultFlag().equals(1)) {
            dryTowerDao.updateTowerNonDefaultFlag(CookieAuthUtils.getCurrentUser());
        }
        CompanyInfo companyInfo = companyInfoMapper.selectBycompanyId(addDryTowerReq.getCompanyId());
        if (companyInfo != null){
            dryTower.setCompany(companyInfo.getCompanyName());
            dryTower.setCompanyId(companyInfo.getCompanyId());
        }
        dryTowerDao.insertSelective(dryTower);
        return TowerOperaResp.builder().towerId(dryTower.getTowerId())
                .status(TowerOperaResp.OPERATION_STATUS.SUCCESS.getValue()).build();
    }

    public JsonResult getSupplierAllInfo() {
        String userId = CookieAuthUtils.getCurrentUser();
        UserInfo userInfo = userInfoDao.getUserInfoByUserId(userId);
        SupplierInfoResp supplierInfoResp = new SupplierInfoResp();
        if (userId == null) {
            log.warn("get supplier info fail due to user no exist");
            return JsonResult
                    .success(SupplierInfoResp.builder().status(SupplierInfoResp.CODE_ENUE.NO_USER.getValue()).build());
        }
        supplierInfoResp.setUserId(userId);
        supplierInfoResp.setIdCard(userInfo.getIdCard());
        supplierInfoResp.setUserName(userInfo.getName());
        supplierInfoResp.setServiceMobile(ServiceMobileConfig.serviceMobile.get(0));
        if (userInfo.getMobile() != null || userInfo.getMobile().size() > 0) {
            supplierInfoResp.setMobile(userInfo.getMobile().iterator().next());
        }
        supplierInfoResp.setStatus(SupplierInfoResp.CODE_ENUE.SUCCESS.getValue());
        List<DryTower> dryTowers = dryTowerDao.selectDryTowerByContactUserId(Sets.newHashSet(userId));
        if (dryTowers == null) {
            supplierInfoResp.setTowerInfos(Collections.EMPTY_LIST);
            return JsonResult.success(supplierInfoResp);
        }
        supplierInfoResp.setCompanyInfo(companyInfoMapper.selectByUserId(Sets.newHashSet(userId)));
        List<SupplierInfoResp.TowerInfo> towerInfos = new ArrayList<>(dryTowers.size());
        dryTowers.stream().forEach(x -> {
            SupplierInfoResp.TowerInfo towerInfo = new SupplierInfoResp.TowerInfo();
            BeanUtils.copyProperties(x, towerInfo);
            if (x.getCommodityId() != null) {
                List<Commodity> commodities = commodityDao.getCommodityByIds(x.getCommodityId());
                towerInfo.setCommoditys(commodities);
            } else {
                towerInfo.setCommoditys(Collections.EMPTY_LIST);
            }
            towerInfos.add(towerInfo);
        });
        supplierInfoResp.setUserNum(CollectionUtils.isNotEmpty(dryTowers.get(0).getContactUserId()) ?
                (dryTowers.get(0).getContactUserId().size() -1) : 0 );
        supplierInfoResp.setTowerInfos(towerInfos);
        supplierInfoResp.setImgs(imgService.getUserImgs(userInfo.getUserId()));
        log.debug("supplier get user info return [{}]", JacksonUtils.obj2String(supplierInfoResp));
        return JsonResult.success(supplierInfoResp);

    }
    public GetSupplierCornInfoResp getSupplierInfo(String userId) {
        GetSupplierCornInfoResp supplierCornInfoResp = new GetSupplierCornInfoResp();

        UserInfo userInfo = userInfoDao.getSupplierUserInfoByUserId(userId);
        if (userInfo == null) {
            supplierCornInfoResp.setOptResult(GetDriverCornInfoResp.STATUS.NO_USER.getValue());
            return supplierCornInfoResp;
        }
        BeanUtils.copyProperties(userInfo, supplierCornInfoResp);
        if (!userInfo.getIdType().equals(IdTypeEnum.IDCARD.getValue())) {
            supplierCornInfoResp.setIdCard("");
        }
        if (userInfo.getMobile() != null && userInfo.getMobile().size() > 0) {
            supplierCornInfoResp.setMobile(userInfo.getMobile().iterator().next());
        }
        supplierCornInfoResp.setOptResult(GetDriverCornInfoResp.STATUS.SUCCESS.getValue());
        supplierCornInfoResp.setImgs(imgService.getUserImgs(userInfo.getUserId()));
        return supplierCornInfoResp;

    }


}
