package com.demai.cornel.demeManager.service;

import com.demai.cornel.dao.CommodityDao;
import com.demai.cornel.dao.TaskInfoDao;
import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.model.TaskInfo;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.purcharse.dao.*;
import com.demai.cornel.purcharse.model.*;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.util.TimeStampUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2020-04-01    14:56
 */
@Service @Slf4j public class SaleConvertTaskService {
    @Resource private CargoInfoMapper cargoInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private FreightInfoMapper freightInfoMapper;
    @Resource private UserInfoDao userInfoDao;
    @Resource private BuyerInfoMapper buyerInfoMapper;
    @Resource private TaskInfoDao taskInfoDao;
    @Resource private CommodityDao commodityDao;
    @Resource private StackOutInfoMapper stackOutInfoMapper;

    private static final String TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";

    public boolean buildTask(StackOutInfo stackOutInfo, SaleOrder saleOrder,StoreInfo storeInfo) {
        CargoInfo cargoInfo = new CargoInfo();
        cargoInfo.setCargoId(UUID.randomUUID().toString());
        cargoInfo.setUnitWeight(stackOutInfo.getUnitWeight());
        cargoInfo.setPrice(saleOrder.getCommodityPrice());
        cargoInfo.setCommodityId(stackOutInfo.getCommodityId());
        cargoInfo.setDealTime(saleOrder.getOrderTime());
        cargoInfo.setStoreId(stackOutInfo.getStoreId());

        int res = cargoInfoMapper.insertSelective(cargoInfo);

        if (res != 1) {
            log.error("sale order buildTask fail due to insert cargo into db err");
            return false;
        }
        stackOutInfo.setCargoId(cargoInfo.getCargoId());
        FreightInfo freightInfo = freightInfoMapper.selectByFreightId(stackOutInfo.getFreightInfoId());
        if (freightInfo == null || freightInfo.getTransportType() == null) {
            log.error("sale order buildTask fail due to transport info err");
            return false;
        }
        if (freightInfo.getTransportType().size() != 1 || !TransportType
                .typeOf(freightInfo.getTransportType().iterator().next()).getType()
                .equals(TransportType.TRANSPORT_TYPE_ENUM.MOTOR.getType())) {
            log.info("sale order buildTask fail due to transport tpe not support transport type is {}",
                    JacksonUtils.obj2String(freightInfo.getTransportType()));
            return true;
        }
        UserInfo storeKeeper = userInfoDao.getUserInfoByUserId(stackOutInfo.getStoreKeeper());
        if (storeKeeper == null) {
            log.error("sale order buildTask fail due to send out  user info cant found ");
            return false;
        }
        BuyerInfo buyerInfo = buyerInfoMapper.selectByUserId(saleOrder.getBuyerId());
        if (buyerInfo == null) {
            log.error("sale order buildTask fail due to buyerInfo  user info cant found ");
            return false;
        }
        Commodity commodity = commodityDao.getCommodityByCommodityId(saleOrder.getCommodityId());
        LocationInfo fromLocation = locationInfoMapper.selectByLocationId(saleOrder.getFromLocation());
        LocationInfo toLocation = locationInfoMapper.selectByLocationId(stackOutInfo.getReceiveLocation());
        if (fromLocation == null || toLocation == null) {
            log.error("sale order buildTask fail due to from loc or to loc lock  from is {},to is {}",
                    JacksonUtils.obj2String(fromLocation), JacksonUtils.obj2String(toLocation));
            return false;
        }
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTitle(commodity.getName() + "-" + fromLocation.getLocation() + "-" + toLocation.getLocation());
        taskInfo.setStartTime(TimeStampUtil.timeStampConvertString(TIME_FORMAT, stackOutInfo.getStartTime()));
        taskInfo.setEndTime(TimeStampUtil.timeStampConvertString(TIME_FORMAT, stackOutInfo.getEndTime()));
        taskInfo.setDistance(new BigDecimal(1000.00));
        taskInfo.setArr(toLocation.getLocation());
        taskInfo.setDep(fromLocation.getLocation());
        taskInfo.setReceiverUserId(Sets.newHashSet(saleOrder.getBuyerId()));
        taskInfo.setReceiverMobile(buyerInfo.getMobile());
        taskInfo.setSendOutUserId(Sets.newHashSet(storeInfo.getStoreKeeper()));
        taskInfo.setSupplierMobile(storeKeeper.getMobile());
        taskInfo.setTaskId(TaskInfo.TASK_PREX_ENUE.SALE.getValue() + UUID.randomUUID().toString());
        taskInfo.setStatus(TaskInfo.STATUS_ENUE.TASK_ING.getValue());
        taskInfo.setWeight(saleOrder.getWeight());
        taskInfo.setUnitWeight(saleOrder.getUnitWeight());
        taskInfo.setEstimatePrice(stackOutInfo.getFreightPrice());
        taskInfo.setUnitPrice(saleOrder.getUnitPrice());
        taskInfo.setUndistWeight(saleOrder.getWeight());
        int resTask = taskInfoDao.save(taskInfo);
        if (resTask != 1) {
            log.error("sale order buildTask fail due to insert task into db err");
            return false;
        }
        stackOutInfoMapper
                .updateTaskIdAndCargoId(taskInfo.getTaskId(), cargoInfo.getCargoId(), stackOutInfo.getOutId());
        return true;
    }
}
