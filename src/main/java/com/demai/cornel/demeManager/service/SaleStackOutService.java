package com.demai.cornel.demeManager.service;

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
    private static final String TIME_TORMAT = "yyyy-MM-dd";

    public AdminReviewSaleResp updateSaleStackOutInfo(AdminReviewSaleReq reviewSaleReq, SaleOrder saleOrder,
            SaleOrder newSaleOrder) {
        StoreInfo storeInfo = storeInfoMapper.selectByStoreId(reviewSaleReq.getStoreId());
        if (storeInfo == null || storeInfo.getUndistWeight().compareTo(saleOrder.getWeight()) == -1) {
            log.debug("updateSaleStackOutInfo fail due to store lock");
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.STAORE_ERR.getValue())
                    .build();
        }

        StackOutInfo oldStackInfo = stackOutInfoMapper.selectByOutId(saleOrder.getOutStackId());
        boolean insert = false;
        if (oldStackInfo == null) {
            oldStackInfo = new StackOutInfo();
            oldStackInfo.setOutId(UUID.randomUUID().toString());
            insert = true;
        }
        oldStackInfo.setFromLocation(storeInfo.getLocationId());
        oldStackInfo.setReceiveLocation(saleOrder.getReceiveLocation());
        oldStackInfo.setFreightInfoId(reviewSaleReq.getFreightId());
        oldStackInfo.setUnitWeight(saleOrder.getUnitWeight());
        oldStackInfo.setUnitPrice(saleOrder.getUnitPrice());
        oldStackInfo.setFreightPrice(freightInfoMapper.selectByFreightId(reviewSaleReq.getFreightId()).getPrice());
        oldStackInfo.setOrderPrice(saleOrder.getOrderPrice());
        oldStackInfo.setCommodityId(saleOrder.getCommodityId());
        oldStackInfo.setBuyerId(saleOrder.getBuyerId());
        oldStackInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(TIME_TORMAT, reviewSaleReq.getOutStartTime()));
        oldStackInfo.setEndTime(TimeStampUtil.stringConvertTimeStamp(TIME_TORMAT, reviewSaleReq.getOutStartTime()));
        oldStackInfo.setOperatorUser(CookieAuthUtils.getCurrentUser());
        oldStackInfo.setReceiveLocation(saleOrder.getReceiveLocation());
        oldStackInfo.setWeight(saleOrder.getWeight());
        oldStackInfo.setStatus(StackOutInfo.STATUS_ENUM.PASS_APPROVAL.getValue());
        int resUpdateSt = storeInfoMapper.updateUndistWeight(storeInfo.getStoreId(), storeInfo.getUndistWeight(),
                storeInfo.getUndistWeight().subtract(saleOrder.getWeight()));
        if(resUpdateSt!=1){
            log.debug("updateSaleStackOutInfo fail due to UPDATE store undist store fail");
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.SERVER_ERR.getValue())
                    .build();
        }
        int res;
        if (insert) {
            res = stackOutInfoMapper.insertSelective(oldStackInfo);
        } else {
            res = stackOutInfoMapper.updateByPrimaryKeySelective(oldStackInfo);
        }
        if (res != 1) {
            log.debug("updateSaleStackOutInfo fail due to UPDATE store lock");
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.SERVER_ERR.getValue())
                    .build();
        }
        newSaleOrder.setOutStackId(oldStackInfo.getOutId());
        newSaleOrder.setFromLocation(oldStackInfo.getFromLocation());
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
