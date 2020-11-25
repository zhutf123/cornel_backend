package com.demai.cornel.purcharse.service;

import com.demai.cornel.dao.CommodityDao;
import com.demai.cornel.dao.LorryInfoDao;
import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.dao.TaskInfoDao;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.model.LorryInfo;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.model.TaskInfo;
import com.demai.cornel.purcharse.dao.LocationInfoMapper;
import com.demai.cornel.purcharse.dao.SaleOrderMapper;
import com.demai.cornel.purcharse.dao.WaybillInfoMapper;
import com.demai.cornel.purcharse.model.CargoInfo;
import com.demai.cornel.purcharse.model.LocationInfo;
import com.demai.cornel.purcharse.model.SaleOrder;
import com.demai.cornel.purcharse.model.WaybillInfo;
import com.demai.cornel.util.*;
import com.google.common.collect.Sets;
import com.hp.gagawa.java.elements.B;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.demai.cornel.service.QuoteService.DATE_TIME_FORMAT;

/**
 * @Author binz.zhang
 * @Date: 2020-02-19    11:36
 */
@Service @Slf4j public class DistSaleOrderService {
    @Resource private CommodityDao commodityDao;
    @Resource private TaskInfoDao taskInfoDao;
    @Resource private LorryInfoDao lorryInfoDao;
    @Resource private WaybillInfoMapper waybillInfoMapper;
    @Resource private OrderInfoDao orderInfoDao;
    @Resource private LocationInfoMapper locationInfoMapper;

    @Resource private SaleOrderMapper saleOrderMapper;



    public String cargoConvertTask(String saleOrderId) {
        SaleOrder saleOrder = saleOrderMapper.selectBySaleId(saleOrderId);
        Commodity commodity = commodityDao.getCommodityByCommodityId(saleOrder.getCommodityId());
        TaskInfo taskInfo = new TaskInfo();
        //taskInfo.setUnitWeight(saleOrder.getUintWeight());
        taskInfo.setEndTime(TimeStampUtil.timeStampConvertString(DATE_TIME_FORMAT, saleOrder.getReceiveEndTime()));
        taskInfo.setDistance(new BigDecimal(1000.00));
        taskInfo.setDistance(new BigDecimal(1000.00));
        taskInfo.setArr(locationInfoMapper.selectByLocationId(saleOrder.getFromLocation()).getLocation());
        taskInfo.setDep(locationInfoMapper.selectByLocationId(saleOrder.getReceiveLocation()).getLocation());
        taskInfo.setProduct(commodity.getName());
        taskInfo.setTitle(commodity.getName() + saleOrder.getFromLocation() + "到" + saleOrder.getReceiveLocation());
        taskInfo.setReceiverUserId(Sets.newHashSet(saleOrder.getBuyerId()));
        taskInfo.setSendOutUserId(Sets.newHashSet("binz.zhang"));
        taskInfo.setSupplierMobile(Sets.newHashSet("13551151842"));
        taskInfo.setTaskId(UUID.randomUUID().toString());
        taskInfo.setStatus(TaskInfo.STATUS_ENUE.TASK_ING.getValue());
        taskInfo.setWeight(saleOrder.getWeight());
        taskInfo.setUnitWeight(saleOrder.getUnitWeight());
        taskInfo.setEstimatePrice(new BigDecimal(1123.00));
        taskInfo.setUndistWeight(new BigDecimal(0.0));
        List<LorryInfo> locationInfoList = lorryInfoDao.getAllIdleLorry();
        if (locationInfoList == null) {
            return "当前无车辆进行派单,请稍后再试";
        }
        List<OrderInfo> orderInfos = new ArrayList<>();
        BigDecimal unditWeght = saleOrder.getWeight();
        List<String> alreadyDistL = new ArrayList<>();
        boolean distRes = true;
        int tryNum = 0;

        log.info("当前有{}量闲置车辆，准备进行派单。派单重量为{}", locationInfoList.size(), saleOrder.getWeight());
        while (unditWeght.compareTo(new BigDecimal(0.0)) == 1 && distRes) {
            tryNum++;
            if (alreadyDistL.size() == locationInfoList.size()) {
                log.info("没有足够的车辆进行派单");
                distRes = false;
            }
            for (LorryInfo lorryInfo : locationInfoList) {
                if (!alreadyDistL.contains(lorryInfo.getLorryId())) {
                    if (lorryInfo.getOverCarryWeight().compareTo(unditWeght) != 1) {
                        orderInfos.add(buildOrder(taskInfo, lorryInfo, lorryInfo.getOverCarryWeight()));
                        log.info("第{}次 尝试派单，车辆ID {},派发重量 {} ", tryNum, lorryInfo.getLorryId(),lorryInfo.getOverCarryWeight().toString());
                        unditWeght = unditWeght.subtract(lorryInfo.getOverCarryWeight());
                        alreadyDistL.add(lorryInfo.getLorryId());
                    } else {
                        orderInfos.add(buildOrder(taskInfo, lorryInfo, unditWeght));
                        alreadyDistL.add(lorryInfo.getLorryId());
                        log.info("第{}次 尝试派单，车辆ID {},派发重量 {} ", tryNum, lorryInfo.getLorryId(), unditWeght.toString());
                        unditWeght = new BigDecimal(0.0);

                    }
                }
            }
        }

        if (distRes && orderInfos != null) {
            taskInfo.setUndistWeight(new BigDecimal(0.0));
            taskInfoDao.save(taskInfo);
            log.info("dist order sucess");
            orderInfos.stream().forEach(x -> {
                orderInfoDao.save(x);
                WaybillInfo waybillInfo = new WaybillInfo();
                waybillInfo.setCargoId(saleOrder.getCargoId());
                waybillInfo.setSaleId(saleOrder.getOrderId());
                waybillInfo.setDeliverId(x.getOrderId());
                waybillInfoMapper.insertSelective(waybillInfo);
            });
            return "dist order success ";

        }
        return "dist order fail ";

    }

    private OrderInfo buildOrder(TaskInfo taskSaveVO, LorryInfo lorryInfo, BigDecimal weight) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(IDUtils.getUUID());
        orderInfo.setTaskId(taskSaveVO.getTaskId());
        orderInfo.setUserId(lorryInfo.getUserId());
        orderInfo.setLorryId(lorryInfo.getLorryId());

        orderInfo.setDistance(new BigDecimal(1000));
        orderInfo.setUnitDistance("km");
        orderInfo.setCarryWeight(lorryInfo.getCarryWeight());
        orderInfo.setOrderWeight(weight);
        orderInfo.setUnitWeight("吨");

        orderInfo.setSendOutUserId(taskSaveVO.getSendOutUserId());
        orderInfo.setReceiverUserId(taskSaveVO.getReceiverUserId());
        orderInfo.setReceiveCode(GenerateCodeUtils.generateRandomCode(4));
        orderInfo.setSendOutCode(GenerateCodeUtils.generateRandomCode(4));

        orderInfo.setAcceptTime(DateFormatUtils.formatDateTime(new Date(System.currentTimeMillis())));
        orderInfo.setStartTime(DateFormatUtils.formatDateTime(new Date(System.currentTimeMillis())));
        orderInfo.setReceiveTime(taskSaveVO.getStartTime());
        orderInfo.setStatus(OrderInfo.STATUS_ENUE.ORDER_INIT.getValue());
        return orderInfo;
    }


}
