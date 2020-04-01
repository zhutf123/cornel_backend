package com.demai.cornel.demeManager.service;

import com.demai.cornel.demeManager.vo.AdminReviewSaleReq;
import com.demai.cornel.purcharse.dao.*;
import com.demai.cornel.purcharse.model.SaleOrder;
import com.demai.cornel.purcharse.model.StackOutInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author binz.zhang
 * @Date: 2020-04-01    11:27
 */
@Slf4j
@Service
public class SaleStackOutService {
    @Resource private SaleOrderMapper saleOrderMapper;
    @Resource private StackOutInfoMapper stackOutInfoMapper;
    @Resource private StoreInfoMapper storeInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private FreightInfoMapper freightInfoMapper;



//    public void updateSaleStackOutInfo(AdminReviewSaleReq reviewSaleReq, SaleOrder saleOrder){
//        StackOutInfo oldStackInfo = stackOutInfoMapper.selectByOutId(saleOrder.getOutStackId());
//        if(oldStackInfo==null)
//        if(saleOrder==null){
//
//        }
//    }
//
//    /**
//     * 构建新的出库信息
//     * @param reviewSaleReq
//     * @param saleOrder
//     * @return
//     */
//    public StackOutInfo buildNewStackOutInfo(AdminReviewSaleReq reviewSaleReq, SaleOrder saleOrder){
//        StackOutInfo stackOutInfo = new StackOutInfo();
//        stackOutInfo.setFromLocation();
//
//
//
//    }
}
