package com.demai.cornel.demeManager.service;

import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.purcharse.dao.*;
import com.demai.cornel.purcharse.model.*;
import com.demai.cornel.purcharse.service.OutStackService;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    10:28
 */
@Service @Slf4j
/*管理员获取审核通过的订单的详情*/ public class AdminApprovedService {
    @Resource private StackOutInfoMapper stackOutInfoMapper;
    @Resource private StoreInfoMapper storeInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private FreightInfoMapper freightInfoMapper;
    @Resource private SaleStackOutService saleStackOutService;
    @Resource private OutStackService outStackService;
    @Resource private SaleOrderMapper saleOrderMapper;

    public JsonResult adminGetSaleList(Integer status, Integer offset, Integer pgSize) {
        List<AdminAppSaleList> saleOrder = saleOrderMapper
                .AdminGetAppSaleOrderList(status, Optional.ofNullable(pgSize).orElse(10),
                        Optional.ofNullable(offset).orElse(0));
        if (saleOrder == null) {
            log.warn("SaleUnderReviewService--adminGetSaleList fail due to order invalid");
            return JsonResult.success(Collections.EMPTY_LIST);
        }
        return JsonResult.success(saleOrder);
    }

    public JsonResult adminGetSaleDetail(String orderId) {
        AdminAppSaleDetail saleDetail = saleOrderMapper.adminGetAppSaleDetail(orderId);
        if (saleDetail == null) {
            log.error("AdminApprovedService -- adminGetSaleDetail cant get sale order detail order id is {}", orderId);
            return JsonResult.success(
                    AdminAppSaleDetail.builder().optStatus(AdminAppSaleDetail.STATUS_ENUE.ORDER_INVALID.getValue())
                            .build());
        }
        if (saleDetail != null && saleDetail.getTransportTypeMap() != null) {
            StringBuilder stringBuilder = new StringBuilder("");
            saleDetail.getTransportTypeMap().stream().forEach(xT -> {
                stringBuilder.append(TransportType.typeOf(xT).getExpr()).append("+");
            });
            saleDetail.setTransportType(stringBuilder.toString());
        }
        if(saleDetail.getShippProcess()!=null){

        }
        saleDetail.setOptStatus(AdminAppSaleDetail.STATUS_ENUE.SUCCESS.getValue());
        return JsonResult.success(saleDetail);
    }
}