package com.demai.cornel.demeManager.service;

import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.model.LoanInfo;
import com.demai.cornel.purcharse.dao.*;
import com.demai.cornel.purcharse.model.*;
import com.demai.cornel.util.CookieAuthUtils;
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
    //    public List<AdminGetSaleList> adminGetSaleByStatus(Integer status, Integer offSet, Integer pgSize) {
    //        List<AdminGetSaleList> saleDetails = saleOrderMapper
    //                .AdminGetSaleOrderList(status, Optional.ofNullable(pgSize).orElse(10),
    //                        Optional.ofNullable(offSet).orElse(0));
    //        return CollectionUtils.isEmpty(saleDetails) ? Collections.EMPTY_LIST : saleDetails;
    //    }

    /***
     * 管理员获取到订单的详情 包括出库的信息以及预计收益
     * @param orderId
     * @return
     */
    public List<AdminGetSaleDetail> adminGetSaleByStatus(Integer status, Integer offset, Integer pgSize) {
        List<AdminGetSaleList> saleOrder = saleOrderMapper
                .AdminGetSaleOrderList(status, Optional.ofNullable(pgSize).orElse(10),
                        Optional.ofNullable(offset).orElse(0));
        if (saleOrder == null) {
            log.warn("adminGetSaleDetail fail due to order invalid");
            return Collections.EMPTY_LIST;
        }
        List<AdminGetSaleDetail> adminGetSaleDetail = new ArrayList<>(saleOrder.size());
        for (AdminGetSaleList list : saleOrder) {
            AdminGetSaleDetail adminGetSaleDetail1 = new AdminGetSaleDetail();
            BeanUtils.copyProperties(list, adminGetSaleDetail1);
            if (Strings.isNullOrEmpty(list.getOutStackId())) {
                continue;
            }
            StackOutInfo stackOutInfo = stackOutInfoMapper.selectByOutId(list.getOutStackId());
            if (stackOutInfo == null) {
                log.debug("adminGetSaleDetail cannot find stackOutInfo from db ");
                continue;
            }
            StoreInfo storeInfo = storeInfoMapper.selectByStoreId(stackOutInfo.getStoreId());
            if (storeInfo == null) {
                log.debug("adminGetSaleDetail cannot find storeInfo from db ");
                continue;
            }
            FreightInfo freightInfo = freightInfoMapper.selectByFreightId(stackOutInfo.getFreightInfoId());
            if (freightInfo != null && freightInfo.getTransportType() != null) {
                StringBuilder stringBuilder = new StringBuilder();
                freightInfo.getTransportType().stream().forEach(x -> {
                    stringBuilder.append(TransportType.typeOf(x).getExpr());
                });
                adminGetSaleDetail1.setTransportType(stringBuilder.toString());
            }
            adminGetSaleDetail1.setStoreId(stackOutInfo.getStoreId());
            adminGetSaleDetail1.setBuyingPrice(storeInfo.getBuyingPrice());
            adminGetSaleDetail1.setCapitalCost(storeInfo.getCapitalCost());
            adminGetSaleDetail1.setFreightId(stackOutInfo.getFreightInfoId());
            adminGetSaleDetail1.setFreightPrice(stackOutInfo.getFreightPrice());
            LocationInfo loanInfo = locationInfoMapper.selectByLocationId(stackOutInfo.getFromLocation());
            adminGetSaleDetail1.setFromLocation(loanInfo == null ? "" : loanInfo.getLocation());
            adminGetSaleDetail.add(adminGetSaleDetail1);
        }
        return adminGetSaleDetail;

    }

    public AdminGetSaleDetail adminGetSaleDetail(String orderId) {
        AdminGetSaleList saleOrder = saleOrderMapper.selectSaleList(orderId);
        if (saleOrder == null) {
            log.warn("adminGetSaleDetail fail due to order invalid");
            return AdminGetSaleDetail.builder().optStatus(AdminGetSaleDetail.STATUS_ENUE.ORDER_INVALID.getValue())
                    .build();
        }
        AdminGetSaleDetail adminGetSaleDetail1 = new AdminGetSaleDetail();
        BeanUtils.copyProperties(saleOrder, adminGetSaleDetail1);
        if (Strings.isNullOrEmpty(saleOrder.getOutStackId())) {
            return adminGetSaleDetail1;
        }
        StackOutInfo stackOutInfo = stackOutInfoMapper.selectByOutId(saleOrder.getOutStackId());
        if (stackOutInfo == null) {
            log.debug("adminGetSaleDetail cannot find stackOutInfo from db ");
            return adminGetSaleDetail1;
        }
        StoreInfo storeInfo = storeInfoMapper.selectByStoreId(stackOutInfo.getStoreId());
        if (storeInfo == null) {
            log.debug("adminGetSaleDetail cannot find storeInfo from db ");
            return adminGetSaleDetail1;
        }

        FreightInfo freightInfo = freightInfoMapper.selectByFreightId(stackOutInfo.getFreightInfoId());
        if (freightInfo != null && freightInfo.getTransportType() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            freightInfo.getTransportType().stream().forEach(x -> {
                stringBuilder.append(TransportType.typeOf(x).getExpr());
            });
            adminGetSaleDetail1.setTransportType(stringBuilder.toString());
        }
        adminGetSaleDetail1.setStoreId(stackOutInfo.getStoreId());
        adminGetSaleDetail1.setBuyingPrice(storeInfo.getBuyingPrice());
        adminGetSaleDetail1.setCapitalCost(storeInfo.getCapitalCost());
        adminGetSaleDetail1.setFreightId(stackOutInfo.getFreightInfoId());
        adminGetSaleDetail1.setFreightPrice(stackOutInfo.getFreightPrice());
        LocationInfo loanInfo = locationInfoMapper.selectByLocationId(stackOutInfo.getFromLocation());
        adminGetSaleDetail1.setFromLocation(loanInfo == null ? "" : loanInfo.getLocation());

        List<FreightInfo> freightInfos = freightInfoMapper
                .selectFreights(stackOutInfo.getReceiveLocation(), stackOutInfo.getFromLocation());
        if (freightInfos != null) {
            List<AdminGetOutStackInfo.OtherInfo> otherInfos = new ArrayList<>(freightInfos.size());
            freightInfos.stream().forEach(fiT -> {
                AdminGetOutStackInfo.OtherInfo otherInfo = new AdminGetOutStackInfo.OtherInfo();
                otherInfo.setFreightId(fiT.getFreightId());
                otherInfo.setFreightPrice(fiT.getPrice());
                if (fiT != null && fiT.getTransportType() != null) {
                    StringBuilder stringBuilder = new StringBuilder("");
                    fiT.getTransportType().stream().forEach(xT -> {
                        stringBuilder.append(TransportType.typeOf(xT).getExpr()).append("+");
                    });
                    otherInfo.setTransportType(
                            stringBuilder.toString().substring(0, stringBuilder.lastIndexOf("+")));
                }
                otherInfo.setInCome(saleOrder.getCommodityPrice().subtract(fiT.getPrice())
                        .subtract(storeInfo.getBuyingPrice()));
                otherInfos.add(otherInfo);
            });
            adminGetSaleDetail1.setFreightAndIncome(otherInfos);
        }
        return adminGetSaleDetail1;
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
                    .selectFreights(saleOrder.getReceiveLocation(), x.getLocationId());
            if (freightInfos != null) {
                List<AdminGetOutStackInfo.OtherInfo> otherInfos = new ArrayList<>(freightInfos.size());
                freightInfos.stream().forEach(freightInfo -> {
                    AdminGetOutStackInfo.OtherInfo otherInfo = new AdminGetOutStackInfo.OtherInfo();
                    otherInfo.setFreightId(freightInfo.getFreightId());
                    otherInfo.setFreightPrice(freightInfo.getPrice());
                    //
                    if (freightInfo != null && freightInfo.getTransportType() != null) {
                        StringBuilder stringBuilder = new StringBuilder("");
                        freightInfo.getTransportType().stream().forEach(xT -> {
                            stringBuilder.append(TransportType.typeOf(xT).getExpr()).append("+");
                        });
                        otherInfo.setTransportType(
                                stringBuilder.toString().substring(0, stringBuilder.lastIndexOf("+")));
                    }
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

    public AdminReviewSaleResp adminReviewSale(AdminReviewSaleReq adminReviewSaleReq) {
        if (adminReviewSaleReq == null || adminReviewSaleReq.getStatus() == null) {
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.PARAM_ERROR.getValue())
                    .build();
        }
        SaleOrder saleOrder = saleOrderMapper.selectBySaleId(adminReviewSaleReq.getOrderId());
        if (saleOrder == null) {
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.ORDER_UN_CHANGE.getValue())
                    .build();
        }
        if (!saleOrder.getViewStatus().equals(SaleOrder.STATUS_VIEW.REJECT_APPROVAL.getValue())) {
            return rejectSaleOrder(adminReviewSaleReq);
        }
        SaleOrder saleOrderNew = new SaleOrder();
        saleOrderNew.setReviewUser(CookieAuthUtils.getCurrentUser());
        saleOrderNew.setOrderId(saleOrder.getOrderId());
        if (adminReviewSaleReq.getStatus().equals(SaleOrder.STATUS_VIEW.REJECT_APPROVAL.getValue())) {
            saleOrderNew.setViewStatus(SaleOrder.STATUS_VIEW.REJECT_APPROVAL.getValue());
            saleOrderNew.setViewStatus(SaleOrder.STATUS_ENUM.REJECT_APPROVAL.getValue());
        }
        return null;
    }

    private AdminReviewSaleResp rejectSaleOrder(AdminReviewSaleReq adminReviewSaleReq) {
        if (adminReviewSaleReq == null || adminReviewSaleReq.getStatus()
                .equals(SaleOrder.STATUS_VIEW.REJECT_APPROVAL.getValue())) {
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.PARAM_ERROR.getValue())
                    .build();
        }
        SaleOrder saleOrderNew = new SaleOrder();
        saleOrderNew.setOrderId(adminReviewSaleReq.getOrderId());
        saleOrderNew.setViewStatus(SaleOrder.STATUS_VIEW.REJECT_APPROVAL.getValue());
        saleOrderNew.setViewStatus(SaleOrder.STATUS_ENUM.REJECT_APPROVAL.getValue());
        HashMap<String, String> reviewOpt = new HashMap<>(2);
        reviewOpt.put("errCode", String.valueOf(adminReviewSaleReq.getErrCode()));
        reviewOpt.put("errDesc", adminReviewSaleReq.getErrDesc());
        saleOrderNew.setReviewOpt(reviewOpt);
        int res = saleOrderMapper.updateByPrimaryKey(saleOrderNew);
        if (res != 1) {
            log.error("rejectSaleOrder err due to update db err");
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.SERVER_ERR.getValue())
                    .build();
        }
        return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.SUCCESS.getValue()).build();
    }

    private AdminReviewSaleResp agreeSaleOrder(AdminReviewSaleReq adminReviewSaleReq, SaleOrder oldSaleOrder) {
        if (adminReviewSaleReq == null || adminReviewSaleReq.getStatus()
                .equals(SaleOrder.STATUS_VIEW.PASS_APPROVAL.getValue())) {
             return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.PARAM_ERROR.getValue())
                    .build();
        }

//        SaleOrder saleOrderNew = new SaleOrder();
//        saleOrderNew.setOrderId(adminReviewSaleReq.getOrderId());
//        saleOrderNew.setViewStatus(SaleOrder.STATUS_VIEW.PASS_APPROVAL.getValue());
//        saleOrderNew.setViewStatus(SaleOrder.STATUS_ENUM.PASS_APPROVAL.getValue());
//        StoreInfo storeInfo = storeInfoMapper.selectByStoreId(adminReviewSaleReq.getStoreId());
//        if (storeInfo == null || storeInfo.getUndistWeight().compareTo(oldSaleOrder.getWeight()) == -1) {
//            log.warn("agreeSaleOrder fail due to store insufficient");
//            return false;
//        }
//        StackOutInfo stackOutInfo = stackOutInfoMapper.selectByOutId(oldSaleOrder.getOutStackId());
//        if (stackOutInfo == null) {
//            StackOutInfo stackOutInfo1 = new StackOutInfo();
//
//        }

        return null;
    }

}