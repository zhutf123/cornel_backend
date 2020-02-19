package com.demai.cornel.purcharse.service;

import com.demai.cornel.dao.CommodityDao;
import com.demai.cornel.dao.TaskInfoDao;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.model.TaskInfo;
import com.demai.cornel.purcharse.model.CargoInfo;
import com.demai.cornel.purcharse.model.SaleOrder;
import com.demai.cornel.util.DateUtils;
import com.demai.cornel.util.TimeStampUtil;
import com.google.common.collect.Sets;
import com.hp.gagawa.java.elements.B;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2020-02-19    11:36
 */
@Service @Slf4j public class DistSaleOrderService {
    @Resource private CommodityDao commodityDao;
    @Resource private TaskInfoDao taskInfoDao;

    private static final String TIME_FORMAT = "yyyy-MM-dd";

    public String cargoConvertTask(CargoInfo cargoInfo, SaleOrder saleOrder) {
        Commodity commodity = commodityDao.getCommodityByCommodityId(cargoInfo.getCommodityId());
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setUnitWeight(saleOrder.getUintWeight());
        taskInfo.setEndTime(TimeStampUtil.timeStampConvertString(TIME_FORMAT, saleOrder.getReceiveEndTime()));
        taskInfo.setDistance(new BigDecimal(1000.00));
        taskInfo.setArr(saleOrder.getFromLocation());
        taskInfo.setDep(saleOrder.getReceiveLocation());
        taskInfo.setProduct(commodity.getName());
        taskInfo.setTitle(commodity.getName()+saleOrder.getFromLocation()+"到"+saleOrder.getReceiveLocation());
        taskInfo.setReceiverUserId(Sets.newHashSet(saleOrder.getBuyerId()));
        taskInfo.setSendOutUserId(Sets.newHashSet("binz.zhang"));
        taskInfo.setSupplierMobile(Sets.newHashSet("13551151842"));
        taskInfo.setTaskId(UUID.randomUUID().toString());
        taskInfoDao.save(taskInfo);
        return taskInfo.getTaskId();
    }

}
