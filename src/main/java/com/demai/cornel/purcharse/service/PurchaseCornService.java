package com.demai.cornel.purcharse.service;

import com.demai.cornel.dao.CommodityDao;
import com.demai.cornel.dao.LorryInfoDao;
import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.dao.TaskInfoDao;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.model.LorryInfo;
import com.demai.cornel.model.TaskInfo;
import com.demai.cornel.purcharse.dao.*;
import com.demai.cornel.purcharse.model.*;
import com.demai.cornel.purcharse.vo.GetSystemOfferResp;
import com.demai.cornel.purcharse.vo.req.GetSaleOrderListReq;
import com.demai.cornel.purcharse.vo.req.GetSystemOfferReq;
import com.demai.cornel.purcharse.vo.req.SubmitMyOfferReq;
import com.demai.cornel.purcharse.vo.req.SystemOfferReq;
import com.demai.cornel.purcharse.vo.resp.ClickMyOfferResp;
import com.demai.cornel.purcharse.vo.resp.ClickSystemOfferResp;
import com.demai.cornel.purcharse.vo.resp.BuyOfferResp;
import com.demai.cornel.purcharse.vo.resp.GetSaleOrderListResp;
import com.demai.cornel.util.*;
import com.demai.cornel.vo.quota.ClickSystemQuoteResp;
import com.demai.cornel.vo.quota.GerQuoteListResp;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.hp.gagawa.java.elements.P;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    14:18
 */
@Slf4j @Service public class PurchaseCornService {
    @Resource private OfferSheetMapper offerSheetMapper;
    @Resource private BuyerInfoMapper buyerInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private CommodityDao commodityDao;
    @Resource private CargoInfoMapper cargoInfoMapper;
    @Resource private SaleOrderMapper saleOrderMapper;
    @Resource private PurchaseInfoMapper purchaseInfoMapper;
    @Resource private LorryInfoDao lorryInfoDao;
    @Resource private TaskInfoDao taskInfoDao;
    @Resource private OrderInfoDao orderInfoDao;
    @Resource private DistSaleOrderService distSaleOrderService;
    private static String TIME_FORMAT = "yyyy-MM-dd";

    /**
     * 获取当前系统报价的接口
     *
     * @param getSystemOfferReq
     * @return
     */
    public List<GetSystemOfferResp> getSystemOfferRespList(GetSystemOfferReq getSystemOfferReq) {
        Preconditions.checkNotNull(getSystemOfferReq);
        List<GetSystemOfferResp> getSystemOfferReqs = offerSheetMapper
                .getSystemOfferSheet(getSystemOfferReq.getOfferId(),
                        Optional.ofNullable(getSystemOfferReq.getPgSize()).orElse(10));
        buildSystemQuoteDetail(getSystemOfferReqs);
        return CollectionUtils.isEmpty(getSystemOfferReqs) ? Lists.newArrayList() : getSystemOfferReqs;

    }

    /**
     * 构建系统报价的报价详情数据
     *
     * @param quoteListResps
     * @return
     */
    void buildSystemQuoteDetail(List<GetSystemOfferResp> quoteListResps) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(quoteListResps)) {
            return;
        }
        quoteListResps.stream().forEach(x -> {
            List<GetSystemOfferResp.Detail> details = new LinkedList<>();
            details.add(new GetSystemOfferResp.Detail("质量标准",  Lists.newArrayList(GerQuoteListResp.convertProperties(x.getProperties()))));
            details.add(new GetSystemOfferResp.Detail("单价",  Lists.newArrayList(x.getPrice().toString() + " /" + x.getUnitPrice())));
            details.add(new GetSystemOfferResp.Detail("注意事项", x.getNotice()));
            x.setDetail(details);
        });
    }

    /**
     * 提交求购信息
     *
     * @param param
     * @return
     */
    public BuyOfferResp submitMyOffer(SubmitMyOfferReq param) {

        if (!checkSubmitMyOffer(param)) {
            return BuyOfferResp.builder().status(BuyOfferResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        String userId = CookieAuthUtils.getCurrentUser();
        BuyerInfo buyerInfo = buyerInfoMapper.selectByUserId(userId);
        if (buyerInfo == null) {
            log.debug("submitMyOffer fail due to buyer info invalid ");
            return BuyOfferResp.builder().status(BuyOfferResp.STATUS_ENUE.USER_ERROR.getValue()).build();
        }
        PurchaseInfo purchaseInfo = new PurchaseInfo();
        BeanUtils.copyProperties(param, purchaseInfo);
        Timestamp receiveStart = TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, param.getReceiveStartTime());
        Timestamp receiveEnd = TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, param.getReceiveEndTime());
        purchaseInfo.setReceiveStartTime(receiveStart);
        purchaseInfo.setReceiveStartTime(receiveEnd);
        purchaseInfo.setBuyerId(userId);
        purchaseInfo.setContactUserName(buyerInfo.getUserName());
        if (buyerInfo.getMobile() != null && buyerInfo.getMobile().size() > 0) {
            purchaseInfo.setMobile(buyerInfo.getMobile().iterator().next());
        }
        purchaseInfo.setPurchaseId(UUID.randomUUID().toString());
        purchaseInfo.setStatus(PurchaseInfo.STATUS_ENUM.UNDER_APPROVAL.getValue());
        int res = purchaseInfoMapper.insertSelective(purchaseInfo);
        if (res != 1) {
            log.debug("submitMyOffer fail due to db error ");
            return BuyOfferResp.builder().status(BuyOfferResp.STATUS_ENUE.SERVER_ERROR.getValue()).build();
        }
        return BuyOfferResp.builder().status(BuyOfferResp.STATUS_ENUE.SUCCESS.getValue())
                .orderStatus(purchaseInfo.getStatus()).orderId(purchaseInfo.getPurchaseId()).build();
    }

    /**
     * 校验求购信息是否合法
     *
     * @param param
     * @return
     */
    private boolean checkSubmitMyOffer(SubmitMyOfferReq param) {
        log.debug("submitMyOffer param is [{}]", JacksonUtils.obj2String(param));
        if (param == null) {
            return false;
        }
        if (Strings.isNullOrEmpty(param.getCommodityId()) || param.getWeight() == null
                || param.getCommodityPrice() == null || Strings.isNullOrEmpty(param.getReceiveLocationId()) || Strings
                .isNullOrEmpty(param.getReceiveStartTime()) || Strings.isNullOrEmpty(param.getReceiveEndTime())) {
            log.debug("checksubmitMyOffer fail  due to param error");
            return false;
        }
        return true;
    }

    public ClickSystemOfferResp clickSystemQuoteResp(String offerId) {
        if (Strings.isNullOrEmpty(offerId)) {
            return ClickSystemOfferResp.builder().status(ClickSystemQuoteResp.STATUS_ENUE.NO_OFFER.getValue()).build();
        }
        BuyerInfo buyerInfo = buyerInfoMapper.selectByUserId(CookieAuthUtils.getCurrentUser());
        if (buyerInfo == null) {
            log.debug("clickSystemQuoteResp fail due to user ");
            return ClickSystemOfferResp.builder().status(ClickSystemQuoteResp.STATUS_ENUE.USER_ERROR.getValue())
                    .build();
        }
        ClickSystemOfferResp clickSystemOfferResp = ClickSystemOfferResp.builder().userName(buyerInfo.getUserName())
                .build();

        List<String> location = new ArrayList<>();
        if (!CollectionUtils.isEmpty(buyerInfo.getFrequentlyLocation())) {
            List<String> locationTemp = locationInfoMapper.getLocationByLocationId(buyerInfo.getFrequentlyLocation());
            if (locationTemp != null) {
                location = locationTemp;
            }
        }
        clickSystemOfferResp.setReceiveLocation(location);
        if (!CollectionUtils.isEmpty(buyerInfo.getMobile())) {
            clickSystemOfferResp.setMobile(buyerInfo.getMobile().iterator().next());
        }
        OfferSheet offerSheet = offerSheetMapper.selectByOfferId(offerId);
        if (offerSheet == null) {
            return ClickSystemOfferResp.builder().status(ClickSystemQuoteResp.STATUS_ENUE.COMMODITY_ERROR.getValue())
                    .build();
        }
        Commodity commodity = commodityDao.getCommodityByCommodityId(offerSheet.getCommodityId());
        if (commodity == null) {
            return ClickSystemOfferResp.builder().status(ClickSystemQuoteResp.STATUS_ENUE.COMMODITY_ERROR.getValue())
                    .build();
        }
        clickSystemOfferResp.setCommodity(commodity);
        BeanUtils.copyProperties(offerSheet, clickSystemOfferResp);
        clickSystemOfferResp.setStatus(ClickSystemOfferResp.STATUS_ENUE.SUCCESS.getValue());
        clickSystemOfferResp.setReceiveStartTime(
                DateFormatUtils.getAfterTime(System.currentTimeMillis(), DateFormatUtils.ISO_DATE_PATTERN, 10));
        clickSystemOfferResp.setReceiveStartTime(
                DateFormatUtils.getAfterTime(System.currentTimeMillis(), DateFormatUtils.ISO_DATE_PATTERN, 15));
        //todo 加入抢单逻辑
        return clickSystemOfferResp;

    }

    public ClickMyOfferResp clickMyOfferResp() {
        BuyerInfo buyerInfo = buyerInfoMapper.selectByUserId(CookieAuthUtils.getCurrentUser());
        if (buyerInfo == null) {
            return ClickMyOfferResp.builder().status(ClickMyOfferResp.STATUS_ENUE.USER_ERROR.getValue()).build();
        }
        if (Strings.isNullOrEmpty(buyerInfo.getDefaultLocation()) && buyerInfo.getFrequentlyLocation() == null) {
            return ClickMyOfferResp.builder().status(ClickMyOfferResp.STATUS_ENUE.SUCCESS.getValue())
                    .receiveLocation("").build();
        }
        if (Strings.isNullOrEmpty(buyerInfo.getDefaultLocation())) {
            buyerInfo.setDefaultLocation(buyerInfo.getFrequentlyLocation().iterator().next());
        }
        LocationInfo locationInfo = locationInfoMapper.selectByLocationId(buyerInfo.getDefaultLocation());
        if (locationInfo == null) {
            return ClickMyOfferResp.builder().status(ClickMyOfferResp.STATUS_ENUE.SUCCESS.getValue())
                    .receiveLocation("").build();
        }
        return ClickMyOfferResp.builder().status(ClickMyOfferResp.STATUS_ENUE.SUCCESS.getValue()).receiveLocation("")
                .build();

    }

    /**
     * 提交购买订单
     *
     * @param offer
     * @return
     */
    public BuyOfferResp submitSystemQuoteResp(SystemOfferReq offer) {
        Preconditions.checkNotNull(offer);
        if (!checkOffet(offer)) {
            return BuyOfferResp.builder().status(BuyOfferResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        OfferSheet offerSheet = null;
        if (Strings.isNullOrEmpty(offer.getOfferId())) {
            offerSheet = offerSheetMapper.selectByOfferId(offer.getOfferId());
        }
        CargoInfo cargoInfo = new CargoInfo();
        cargoInfo.setCommodityId(offer.getCommodityId());
        cargoInfo.setDealTime(new Timestamp(System.currentTimeMillis()));
        cargoInfo.setPrice(offer.getCommodityPrice());
        cargoInfo.setUnitWeight(offer.getUnitWeight());
        cargoInfo.setWeight(offer.getWeight());
        cargoInfo.setUnitWeight(offer.getUnitWeight());
        cargoInfo.setCargoId(UUID.randomUUID().toString());
        cargoInfoMapper.insertSelective(cargoInfo);
        SaleOrder saleOrder = new SaleOrder();
        BeanUtils.copyProperties(offer, saleOrder);
        saleOrder.setCargoId(cargoInfo.getCargoId());
        saleOrder.setOrderId(UUID.randomUUID().toString());
        saleOrder.setBuyerId(CookieAuthUtils.getCurrentUser());
        saleOrder.setReceiveStartTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offer.getReceiveStartTime()));
        saleOrder.setReceiveEndTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offer.getReceiveStartTime()));
        if (offerSheet != null) {
            saleOrder.setFromLocation(offerSheet.getLocationId());
        }
        saleOrder.setStatus(SaleOrder.STATUS_ENUM.UNDER_APPROVAL.getValue());
        int ret = saleOrderMapper.insertSelective(saleOrder);
        if (ret != 1) {
            return BuyOfferResp.builder().status(BuyOfferResp.STATUS_ENUE.SERVER_ERROR.getValue()).build();
        }
        return BuyOfferResp.builder().status(BuyOfferResp.STATUS_ENUE.SUCCESS.getValue()).
                orderId(saleOrder.getOrderId()).orderStatus(saleOrder.getStatus()).build();
    }

    public List<GetSaleOrderListResp> getSaleOrderListRespList(GetSaleOrderListReq getSaleOrderListReq) {
        if (getSaleOrderListReq == null) {
            log.debug("getSaleOrderListRespList fail due to getSaleOrderListReq empty");
            return Collections.EMPTY_LIST;
        }
        if (getSaleOrderListReq.getPgSize() == null) {
            getSaleOrderListReq.setPgSize(10);
        }
        List<GetSaleOrderListResp> getSaleOrderListResps = saleOrderMapper
                .getSaleOrderList(getSaleOrderListReq.getOrderId(), getSaleOrderListReq.getPgSize(),
                        getSaleOrderListReq.getOrderType());
        if (getSaleOrderListReq == null) {
            log.debug("getSaleOrderListRespList fail due to get result  empty");
            return Collections.EMPTY_LIST;
        }
        return getSaleOrderListResps;

    }

    /**
     * 提交购买订单
     *
     * @return
     */
    //    public BuyOfferResp getSaleOrderList(SystemOfferReq offer) {
    //
    //    }
    //    public String distOrder(String cargoId) {
    //        List<CargoInfo> cargoInfo = cargoInfoMapper.selectByParentCargoId(cargoId);
    //        if (cargoInfo == null) {
    //            return "cargo invalid";
    //        }
    //        List<LorryInfo>lorryInfos = lorryInfoDao.getAllIdleLorry();
    //        if(lorryInfos==null || lorryInfos.size()<cargoInfo.size()){
    //            return "没有足够的车辆进行派单";
    //        }
    //        SaleOrder saleOrder = saleOrderMapper.selectSaleOrderByCargoId(cargoId);
    //        if(saleOrder==null){
    //            return "购买订单无效";
    //        }
    //        cargoInfo.stream().forEach(x->{
    //            String taskId = distSaleOrderService.cargoConvertTask(x,saleOrder);
    //            String lorryId = null;
    //        });
    //
    //    }
    private boolean checkOffet(SystemOfferReq offerReq) {
        log.debug("checkOffet offerReq is [{}]", JacksonUtils.obj2String(offerReq));
        if (offerReq == null) {
            return false;
        }
        if (Strings.isNullOrEmpty(offerReq.getCommodityId()) || Strings.isNullOrEmpty(offerReq.getContactUserName())
                || Strings.isNullOrEmpty(offerReq.getMobile()) || Strings.isNullOrEmpty(offerReq.getReceiveLocationId())
                || null == offerReq.getWeight() || null == offerReq.getCommodityPrice() || Strings
                .isNullOrEmpty(offerReq.getReceiveStartTime()) || Strings.isNullOrEmpty(offerReq.getReceiveEndTime())) {
            return false;
        }
        return true;

    }
}
