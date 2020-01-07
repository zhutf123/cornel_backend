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
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.model.DryTower;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.util.*;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.supplier.SupplierInfoResp;
import com.demai.cornel.vo.supplier.SupplierRegisterReq;
import com.demai.cornel.vo.tower.TowerOperaResp;
import com.google.common.base.Strings;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.vo.delivery.DeliveryTaskListResp;
import com.demai.cornel.vo.order.GetOrderInfoReq;
import com.demai.cornel.vo.order.OperationOrderReq;
import com.demai.cornel.vo.order.OperationOrderResp;
import com.demai.cornel.vo.supplier.SupplierTaskListResp;
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
                .orderId(param.getOrderId()).realWeight(orderInfo.getCarryWeight().longValue())
                .orderStatus(orderInfo.getStatus()).build();
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
        orderInfo.setCarryWeight(new BigDecimal(param.getRealWeight()));
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
                    .opResult(OperationOrderResp.SUPPLIER_RESP_STATUS_ENUE.OPERATION_ERROR.getValue()).realWeight(0L)
                    .orderId(param.getOrderId()).orderStatus(orderInfo.getStatus()).build();
        }
        orderInfo = orderInfoDao.getOrderInfoByOrderId(param.getOrderId());
        return OperationOrderResp.builder().sendOutTime(DateFormatUtils.formatDateTime(new Date()))
                .success(Boolean.TRUE).opResult(OperationOrderResp.SUPPLIER_RESP_STATUS_ENUE.SUCCESS.getValue())
                .orderId(param.getOrderId()).realWeight(orderInfo.getCarryWeight().longValue())
                .orderStatus(orderInfo.getStatus()).build();
    }

    /**
     * 烘干塔注册
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
            dryTower.getTowerInfos().stream().forEach(x -> {
                DryTower dryTowerInsert = new DryTower();
                dryTowerInsert.setTowerId(UUID.randomUUID().toString());
                BeanUtils.copyProperties(dryTower, dryTowerInsert);
                BeanUtils.copyProperties(x, dryTowerInsert);
                dryTowerDao.insertSelective(dryTowerInsert);
            });

        } catch (Exception e) {
            log.error("dry tower register fail ", e);
            return DryTower.REGISTER_STATUS.SERVICE_ERROR;
        }
        return DryTower.REGISTER_STATUS.SUCCESS;
    }

    /**
     * 校验烘干塔参数
     * todo 其他校验细节还需讨论
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
     * 获取指定人下面的烘干塔列表
     *
     * @param userId
     * @return
     */
    public List<DryTower> getTowerInfoByUserId(String userId) {
        return dryTowerDao.selectDryTowerByUserId(userId);
    }

    /**
     * 根据烘干塔ID 获取烘干塔信息
     *
     * @param towerId
     * @return
     */
    public DryTower getTowerInfoByTowerId(String towerId) {
        return dryTowerDao.selectByTowerId(towerId);
    }

    /**
     * 更新烘干塔信息
     *
     * @param dryTower
     * @return
     */
    public TowerOperaResp updateTowerInfo(DryTower dryTower) {
        if (dryTower == null || Strings.isNullOrEmpty(dryTower.getTowerId())) {
            return TowerOperaResp.builder().status(TowerOperaResp.OPERATION_STATUS.PARAM_ERROR.getValue()).build();
        }
        if (dryTower.getDefaultFlag() != null && dryTower.getDefaultFlag().equals(1)) {
            dryTowerDao.updateTowerNonDefaultFlag(CookieAuthUtils.getCurrentUser());
        }
        int rest = dryTowerDao.updateByPrimaryKeySelective(dryTower);
        if (rest == 0) {
            return TowerOperaResp.builder().status(TowerOperaResp.OPERATION_STATUS.NO_TOWER.getValue()).build();
        }
        return TowerOperaResp.builder().status(TowerOperaResp.OPERATION_STATUS.SUCCESS.getValue())
                .towerId(dryTower.getTowerId()).build();

    }

    /**
     * 烘干塔侧增加新的烘干塔
     *
     * @param dryTower
     * @return
     */
    public TowerOperaResp adddTowerInfo(DryTower dryTower) {
        if (dryTower == null || Strings.isNullOrEmpty(dryTower.getLocationArea()) || Strings
                .isNullOrEmpty(dryTower.getLocationDetail())) {
            return TowerOperaResp.builder().status(TowerOperaResp.OPERATION_STATUS.PARAM_ERROR.getValue()).build();
        }

        dryTower.setTowerId(UUID.randomUUID().toString());
        dryTower.setBindUserId(CookieAuthUtils.getCurrentUser());
        UserInfo userInfo = userInfoDao.getUserInfoByUserId(CookieAuthUtils.getCurrentUser());
        if (userInfo == null || userInfo.getRole() == null || !userInfo.getRole()
                .equals(UserInfo.ROLE_ENUE.SUPPLIER.getValue())) {
            return TowerOperaResp.builder().status(TowerOperaResp.OPERATION_STATUS.AUTH_ERROR.getValue()).build();
        }
        dryTower.setUserIdCard(userInfo.getIdCard());
        dryTower.setContactsName(userInfo.getName());
        Set<String> mobiles = userInfo.getMobile();
        if (mobiles != null && mobiles.size() > 0) {
            dryTower.setContactMobile(mobiles.iterator().next());
        }
        if (dryTower.getDefaultFlag() != null && dryTower.getDefaultFlag().equals(1)) {
            dryTowerDao.updateTowerNonDefaultFlag(CookieAuthUtils.getCurrentUser());
        }
        int rest = dryTowerDao.insertSelective(dryTower);
        if (rest == 0) {
            return TowerOperaResp.builder().status(TowerOperaResp.OPERATION_STATUS.NO_TOWER.getValue()).build();
        }
        return TowerOperaResp.builder().status(TowerOperaResp.OPERATION_STATUS.SUCCESS.getValue())
                .towerId(dryTower.getTowerId()).build();

    }

    public JsonResult getSupplierInfo() {
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
        if(userInfo.getMobile()!=null || userInfo.getMobile().size()>0){
            supplierInfoResp.setMobile(userInfo.getMobile().iterator().next());
        }
        supplierInfoResp.setStatus(SupplierInfoResp.CODE_ENUE.SUCCESS.getValue());
        List<DryTower> dryTowers = dryTowerDao.selectDryTowerByUserId(userId);
        if (dryTowers == null) {
            supplierInfoResp.setTowerInfos(Collections.EMPTY_LIST);
            return JsonResult.success(supplierInfoResp);
        }
        List<SupplierInfoResp.TowerInfo> towerInfos = new ArrayList<>(dryTowers.size());
        dryTowers.stream().forEach(x -> {
            List<Commodity> commodities = commodityDao.getCommodityByIds(x.getCommodityId());
            SupplierInfoResp.TowerInfo towerInfo = new SupplierInfoResp.TowerInfo();
            BeanUtils.copyProperties(x,towerInfo);
            towerInfo.setCommoditys(commodities);
            towerInfos.add(towerInfo);
        });
        supplierInfoResp.setTowerInfos(towerInfos);
        log.debug("supplier get user info return [{}]",JacksonUtils.obj2String(supplierInfoResp));
        return JsonResult.success(supplierInfoResp);

    }

}
