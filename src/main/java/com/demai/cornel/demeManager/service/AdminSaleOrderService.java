package com.demai.cornel.demeManager.service;

import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.model.LoanInfo;
import com.demai.cornel.purcharse.dao.*;
import com.demai.cornel.purcharse.model.*;
import com.demai.cornel.util.JacksonUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    10:00
 */
@Service @Slf4j public class AdminSaleOrderService {
    @Resource private SaleOrderMapper saleOrderMapper;
    @Resource private StackOutInfoMapper stackOutInfoMapper;
    @Resource private StoreInfoMapper storeInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private FreightInfoMapper freightInfoMapper;

    public List<AdminGetSaleListResp> getSaleView() {
        List<AdminGetSaleListResp> saleListResps = saleOrderMapper.selectSaleView();
        return CollectionUtils.isEmpty(saleListResps) ? Collections.EMPTY_LIST : saleListResps;
    }

    /**
     * 管理员获取到订单的简要信息 不包含出库信息
     *
     * @param status
     * @param offSet
     * @param pgSize
     * @return
     */
    public List<AdminGetSaleList> adminGetSaleByStatus(Integer status, Integer offSet, Integer pgSize) {
        List<AdminGetSaleList> saleDetails = saleOrderMapper
                .AdminGetSaleOrderList(status,
                        Optional.ofNullable(pgSize).orElse(10),Optional.ofNullable(offSet).orElse(0));
        return CollectionUtils.isEmpty(saleDetails) ? Collections.EMPTY_LIST : saleDetails;
    }

    /***
     * 管理员获取到订单的详情 包括出库的信息以及预计收益
     * @param orderId
     * @return
     */
    public AdminGetSaleDetail adminGetSaleDetail(String orderId) {
        AdminGetSaleList saleOrder = saleOrderMapper.selectSaleList(orderId);
        if (saleOrder == null) {
            log.warn("adminGetSaleDetail fail due to order invalid");
            return AdminGetSaleDetail.builder().optStatus(AdminGetSaleDetail.STATUS_ENUE.ORDER_INVALID.getValue())
                    .build();
        }
        AdminGetSaleDetail adminGetSaleDetail = new AdminGetSaleDetail();
        BeanUtils.copyProperties(saleOrder, adminGetSaleDetail);
        if (Strings.isNullOrEmpty(saleOrder.getOutStackId())) {
            return adminGetSaleDetail;
        }
        StackOutInfo stackOutInfo = stackOutInfoMapper.selectByOutId(saleOrder.getOutStackId());
        if (stackOutInfo == null) {
            log.debug("adminGetSaleDetail cannot find stackOutInfo from db ");
            return adminGetSaleDetail;
        }
        StoreInfo storeInfo = storeInfoMapper.selectByStoreId(stackOutInfo.getStoreId());
        if (storeInfo == null) {
            log.debug("adminGetSaleDetail cannot find storeInfo from db ");
            return adminGetSaleDetail;
        }
        FreightInfo freightInfo = freightInfoMapper.selectByFreightId(stackOutInfo.getFreightInfoId());
        if(freightInfo!=null && freightInfo.getTransportType()!=null ){
            StringBuilder stringBuilder = new StringBuilder();
            freightInfo.getTransportType().stream().forEach(x->{
                stringBuilder.append(TransportType.typeOf(x).getExpr());
            });
            adminGetSaleDetail.setTransportType(stringBuilder.toString());
        }
        adminGetSaleDetail.setStoreId(stackOutInfo.getStoreId());
        adminGetSaleDetail.setBuyingPrice(storeInfo.getBuyingPrice());
        adminGetSaleDetail.setCapitalCost(storeInfo.getCapitalCost());
        adminGetSaleDetail.setFreightId(stackOutInfo.getFreightInfoId());
        adminGetSaleDetail.setFreightPrice(stackOutInfo.getFreightPrice());
        LocationInfo loanInfo = locationInfoMapper.selectByLocationId(stackOutInfo.getFromLocation());
        adminGetSaleDetail.setFromLocation(loanInfo == null ? "" : loanInfo.getLocation());
        return adminGetSaleDetail;
    }

    /**
     * 管理员获取其他的出货选择
     *
     * @param saleId
     * @return
     */
    public List<AdminGetOutStackInfo> adminGetStackOption(String saleId) {
        SaleOrder saleOrder = saleOrderMapper.selectBySaleId(saleId);
        if (saleOrder == null) {
            log.debug("adminGetStackOption get sale invalid");
            return Collections.EMPTY_LIST;
        }
        List<StoreInfo> storeInfos = storeInfoMapper
                .selectStoreIdByCommodityIdAndWeight(saleOrder.getCommodityId(), saleOrder.getWeight());
        List<AdminGetOutStackInfo> rest = new ArrayList<>(storeInfos.size());
        storeInfos.stream().forEach(x -> {
            AdminGetOutStackInfo temp = new AdminGetOutStackInfo();
            temp.setBuyingPrice(x.getBuyingPrice());
            temp.setStoreId(x.getStoreId());
            LocationInfo locationInfo = locationInfoMapper.selectByLocationId(x.getLocationId());
            temp.setFromLocation(locationInfo == null ? "" : locationInfo.getLocation());
            List<FreightInfo> freightInfos = freightInfoMapper
                    .selectFreights(saleOrder.getFromLocation(), x.getLocationId());
            if (freightInfos != null) {
                List<AdminGetOutStackInfo.OtherInfo> otherInfos = new ArrayList<>(freightInfos.size());
                freightInfos.stream().forEach(freightInfo -> {
                    AdminGetOutStackInfo.OtherInfo otherInfo = new AdminGetOutStackInfo.OtherInfo();
                    otherInfo.setFreightId(freightInfo.getFreightId());
                    otherInfo.setFreightPrice(freightInfo.getPrice());
                    otherInfo.setTransportType(TransportType.typeOf(freightInfo.getTransportType()).getExpr());
                    otherInfo.setInCome(saleOrder.getCommodityPrice().subtract(freightInfo.getPrice())
                            .subtract(x.getBuyingPrice()));
                    otherInfos.add(otherInfo);
                });
                temp.setFreightAndIncome(otherInfos);
                rest.add(temp);
            }
        });
        log.debug("adminGetStackOption get stock option {}", JacksonUtils.obj2String(rest));
        return rest;
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