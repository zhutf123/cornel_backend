package com.demai.cornel.demeManager.service;

import com.demai.cornel.demeManager.model.ShippProcess;
import com.demai.cornel.demeManager.vo.AdminReviewSaleReq;
import com.demai.cornel.demeManager.vo.AdminReviewSaleResp;
import com.demai.cornel.purcharse.dao.*;
import com.demai.cornel.purcharse.model.SaleOrder;
import com.demai.cornel.purcharse.model.StackOutInfo;
import com.demai.cornel.purcharse.model.StoreInfo;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.TimeStampUtil;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.jdbc.TimestampUtils;
import org.redisson.api.annotation.REntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2020-04-01    11:27
 */
@Slf4j @Service public class SaleStackOutService {
    @Resource private SaleOrderMapper saleOrderMapper;
    @Resource private StackOutInfoMapper stackOutInfoMapper;
    @Resource private StoreInfoMapper storeInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private FreightInfoMapper freightInfoMapper;
    @Resource private SaleConvertTaskService saleConvertTaskService;
    private static final String TIME_TORMAT = "yyyy-MM-dd";

    public AdminReviewSaleResp updateSaleStackOutInfo(AdminReviewSaleReq reviewSaleReq, SaleOrder saleOrder,
            SaleOrder newSaleOrder) {
        StoreInfo storeInfo = storeInfoMapper.selectByStoreId(reviewSaleReq.getStoreId());
        if (storeInfo == null || storeInfo.getUndistWeight().compareTo(saleOrder.getWeight()) == -1) {
            log.debug("updateSaleStackOutInfo fail due to store lock");
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.STAORE_ERR.getValue())
                    .build();
        }

        StackOutInfo stackInfo = stackOutInfoMapper.selectByOutId(saleOrder.getOutStackId());
        boolean insert = false;
        if (stackInfo == null) {
            stackInfo = new StackOutInfo();
            stackInfo.setOutId(UUID.randomUUID().toString());
            insert = true;
        }
        stackInfo.setFromLocation(storeInfo.getLocationId());
        stackInfo.setReceiveLocation(saleOrder.getReceiveLocation());
        stackInfo.setFreightInfoId(reviewSaleReq.getFreightId());
        stackInfo.setUnitWeight(saleOrder.getUnitWeight());
        stackInfo.setUnitPrice(saleOrder.getUnitPrice());
        stackInfo.setFreightPrice(freightInfoMapper.selectByFreightId(reviewSaleReq.getFreightId()).getPrice());
        stackInfo.setOrderPrice(saleOrder.getOrderPrice());
        stackInfo.setCommodityId(saleOrder.getCommodityId());
        stackInfo.setBuyerId(saleOrder.getBuyerId());
        stackInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(TIME_TORMAT, reviewSaleReq.getOutStartTime()));
        stackInfo.setEndTime(TimeStampUtil.stringConvertTimeStamp(TIME_TORMAT, reviewSaleReq.getOutEndTime()));
        stackInfo.setOperatorUser(CookieAuthUtils.getCurrentUser());
        stackInfo.setReceiveLocation(saleOrder.getReceiveLocation());
        stackInfo.setWeight(saleOrder.getWeight());
        stackInfo.setStoreId(reviewSaleReq.getStoreId());
        stackInfo.setStatus(StackOutInfo.STATUS_ENUM.PASS_APPROVAL.getValue());
        stackInfo.setShippProcess(reviewSaleReq.getShippProcess());
        int resUpdateSt = storeInfoMapper.updateUndistWeight(storeInfo.getStoreId(), storeInfo.getUndistWeight(),
                storeInfo.getUndistWeight().subtract(saleOrder.getWeight()));
        if (resUpdateSt != 1) {
            log.debug("updateSaleStackOutInfo fail due to UPDATE store undist store fail");
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.SERVER_ERR.getValue())
                    .build();
        }
        int res;
        if (insert) {
            res = stackOutInfoMapper.insertSelective(stackInfo);
        } else {
            res = stackOutInfoMapper.updateByPrimaryKeySelective(stackInfo);
        }
        if (res != 1) {
            log.debug("updateSaleStackOutInfo fail due to UPDATE store lock");
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.SERVER_ERR.getValue())
                    .build();
        }
        newSaleOrder.setOutStackId(stackInfo.getOutId());
        newSaleOrder.setFromLocation(stackInfo.getFromLocation());
        newSaleOrder.setEsIncome(saleOrder.getCommodityPrice().subtract(storeInfo.getBuyingPrice()).subtract(stackInfo.getFreightPrice()));
        if(reviewSaleReq.getShippProcess().equals(ShippProcess.TYPE.PAY_DELIVE.getValue())){
            newSaleOrder.setViewStatus(SaleOrder.STATUS_VIEW.UNDER_PAY.getValue());
        }
        if (reviewSaleReq.getShippProcess().equals(ShippProcess.TYPE.DELIVE_PAY.getValue()) && !saleConvertTaskService.buildTask(stackInfo, saleOrder,storeInfo)) {
            log.error(" updateSaleStackOutInfo 发布任务到大活宝失败 fail");
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.SERVER_ERR.getValue()).build();
        }
        int reSl = saleOrderMapper.updateByPrimaryKeySelective(newSaleOrder);
        if(reSl!=1){
            log.error("updateSaleStackOutInfo fail due to UPDATE sale  fail");
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.SERVER_ERR.getValue())
                    .build();
        }
        return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.SUCCESS.getValue()).build();
    }

    /**
     * 构建新的出库信息
     *
     * @param reviewSaleReq
     * @param saleOrder
     * @return
     */
    //    public StackOutInfo buildNewStackOutInfo(AdminReviewSaleReq reviewSaleReq, SaleOrder saleOrder) {
    //        StackOutInfo stackOutInfo = new StackOutInfo();
    //        stackOutInfo.setFromLocation();
    //
    //    }
}
