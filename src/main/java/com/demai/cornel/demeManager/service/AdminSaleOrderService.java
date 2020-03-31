package com.demai.cornel.demeManager.service;

import com.demai.cornel.demeManager.vo.AdminGetSaleDetail;
import com.demai.cornel.demeManager.vo.AdminGetSaleListResp;
import com.demai.cornel.demeManager.vo.AdminReviewSaleResp;
import com.demai.cornel.purcharse.dao.SaleOrderMapper;
import com.demai.cornel.purcharse.model.SaleOrder;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    10:00
 */
@Service @Slf4j public class AdminSaleOrderService {
    @Resource private SaleOrderMapper saleOrderMapper;

    public List<AdminGetSaleListResp> getSaleView() {
        List<AdminGetSaleListResp> saleListResps = saleOrderMapper.selectSaleView();
        return CollectionUtils.isEmpty(saleListResps) ? Collections.EMPTY_LIST : saleListResps;
    }

    public List<AdminGetSaleDetail> adminGetSaleByStatus(Integer status, Integer offSet, Integer pgSize) {
        List<AdminGetSaleDetail> saleDetails = saleOrderMapper
                .AdminGetSaleOrderList(status, Optional.ofNullable(offSet).orElse(0),
                        Optional.ofNullable(pgSize).orElse(10));
        return CollectionUtils.isEmpty(saleDetails) ? Collections.EMPTY_LIST : saleDetails;
    }

    public AdminReviewSaleResp adminReviewSaleStatus(Integer status, String saleId) {
        //        if(Strings.isNullOrEmpty(saleId)){
        //            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        //        }
        //        SaleOrder saleOrder = saleOrderMapper.selectBySaleId(saleId);
        //        if(saleOrder==null ){
        //            log.warn("adminReviewSaleStatus fail due to ");
        //            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        //
        //        }|| saleOrder.getViewStatus().equals(SaleOrder.STATUS_VIEW.RUNNING.getValue())
        //    }
        return null;

    }
}