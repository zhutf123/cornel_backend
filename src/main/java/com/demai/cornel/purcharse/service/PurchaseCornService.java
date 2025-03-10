package com.demai.cornel.purcharse.service;

import com.demai.cornel.dao.CommodityDao;
import com.demai.cornel.dao.LorryInfoDao;
import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.dao.TaskInfoDao;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.model.LorryInfo;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.model.TaskInfo;
import com.demai.cornel.purcharse.dao.*;
import com.demai.cornel.purcharse.model.*;
import com.demai.cornel.purcharse.vo.GetSystemOfferResp;
import com.demai.cornel.purcharse.vo.req.*;
import com.demai.cornel.purcharse.vo.resp.*;
import com.demai.cornel.util.*;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.quota.ClickSystemQuoteResp;
import com.demai.cornel.vo.quota.GerQuoteListResp;
import com.demai.cornel.vo.task.GetOrderListResp;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.hp.gagawa.java.elements.P;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.jdbc.TimestampUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource private WaybillInfoMapper waybillInfoMapper;
    @Resource private OrderInfoDao orderInfoDao;
    @Resource private SpecialSaleInfoMapper specialSaleInfoMapper;

    @Resource private OutStackService outStackService;
    private static String TIME_FORMAT = "yyyy-MM-dd";
    private static List<BigDecimal> PURCHASE_BARGAIN = new ArrayList<>();

    static {
        PURCHASE_BARGAIN.add(new BigDecimal(10));
        PURCHASE_BARGAIN.add(new BigDecimal(15));
        PURCHASE_BARGAIN.add(new BigDecimal(20));
        PURCHASE_BARGAIN.add(new BigDecimal(50));
    }

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
        if (getSystemOfferReq == null) {
            return Collections.EMPTY_LIST;
        }
        List<GetSystemOfferResp> specialSale = specialSaleInfoMapper
                .getSystemOfferSheet(CookieAuthUtils.getCurrentUser());
        HashMap<String, BigDecimal> specialMap = new HashMap<>();
        if (specialSale != null) {
            specialMap = (HashMap<String, BigDecimal>) specialSale.stream()
                    .collect(Collectors.toMap(GetSystemOfferResp::getCommodityId, GetSystemOfferResp::getPrice));
        }
        HashMap<String, BigDecimal> finalSpecialMap = specialMap;
        getSystemOfferReqs.stream().forEach(x -> {
            if (finalSpecialMap.containsKey(x.getCommodityId())) {
                x.setPrice(finalSpecialMap.get(x.getCommodityId()));
            }
            List<GetSystemOfferResp.Detail> details = new LinkedList<>();
            details.add(new GetSystemOfferResp.Detail("质量标准",
                    Lists.newArrayList(GerQuoteListResp.convertProperties(x.getProperties()))));
            details.add(new GetSystemOfferResp.Detail("单价",
                    Lists.newArrayList(x.getPrice().toString() + " /" + x.getUnitPrice())));
            details.add(new GetSystemOfferResp.Detail("注意事项", x.getNotice()));
            x.setDetail(details);
        });
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
            details.add(new GetSystemOfferResp.Detail("质量标准",
                    Lists.newArrayList(GerQuoteListResp.convertProperties(x.getProperties()))));
            details.add(new GetSystemOfferResp.Detail("单价",
                    Lists.newArrayList(x.getPrice().toString() + " /" + x.getUnitPrice())));
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
        purchaseInfo.setReceiveEndTime(receiveEnd);
        purchaseInfo.setBuyerId(userId);
        purchaseInfo.setContactUserName(buyerInfo.getUserName());
        if (buyerInfo.getMobile() != null && buyerInfo.getMobile().size() > 0) {
            purchaseInfo.setMobile(buyerInfo.getMobile().iterator().next());
        }
        purchaseInfo.setPurchaseId(UUID.randomUUID().toString());
        purchaseInfo.setStatus(PurchaseInfo.STATUS_ENUM.UNDER_PRO.getValue());
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
        if (Strings.isNullOrEmpty(param.getCommodityId()) || param.getWeight() == null || param.getPrice() == null
                || Strings.isNullOrEmpty(param.getReceiveLocationId()) || Strings
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
        clickSystemOfferResp.setReceiveEndTime(
                DateFormatUtils.getAfterTime(System.currentTimeMillis(), DateFormatUtils.ISO_DATE_PATTERN, 15));
        //todo 加入抢单逻辑
        SpecialSaleInfo specialSaleInfo = specialSaleInfoMapper
                .selectSpecilaByCommodityIdAndUserId(clickSystemOfferResp.getCommodity().getCommodityId(),
                        CookieAuthUtils.getCurrentUser());
        if (specialSaleInfo != null && specialSaleInfo.getPrice() != null) {
            clickSystemOfferResp.setPrice(specialSaleInfo.getPrice());
        }
        return clickSystemOfferResp;

    }

    public ClickMyOfferResp clickMyOfferResp() {
        BuyerInfo buyerInfo = buyerInfoMapper.selectByUserId(CookieAuthUtils.getCurrentUser());
        if (buyerInfo == null) {
            return ClickMyOfferResp.builder().status(ClickMyOfferResp.STATUS_ENUE.USER_ERROR.getValue()).build();
        }
        if (Strings.isNullOrEmpty(buyerInfo.getDefaultLocation()) && buyerInfo.getFrequentlyLocation() == null) {
            LocationInfo defaultLocation = locationInfoMapper.selectByLocationId(buyerInfo.getDefaultLocation());
            if (defaultLocation != null) {
                return ClickMyOfferResp.builder().status(ClickMyOfferResp.STATUS_ENUE.SUCCESS.getValue())
                        .receiveLocation(defaultLocation.getLocation()).build();
            }
        }
        if (Strings.isNullOrEmpty(buyerInfo.getDefaultLocation())) {
            buyerInfo.setDefaultLocation(buyerInfo.getFrequentlyLocation().iterator().next());
        }
        LocationInfo locationInfo = locationInfoMapper.selectByLocationId(buyerInfo.getDefaultLocation());
        if (locationInfo == null) {
            return ClickMyOfferResp.builder().status(ClickMyOfferResp.STATUS_ENUE.SUCCESS.getValue())
                    .receiveLocation("").build();
        }
        return ClickMyOfferResp.builder().status(ClickMyOfferResp.STATUS_ENUE.SUCCESS.getValue())
                .receiveLocation(locationInfo.getLocation()).build();

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
        if (!Strings.isNullOrEmpty(offer.getOfferId())) {
            offerSheet = offerSheetMapper.selectByOfferId(offer.getOfferId());
        }
        if (offerSheet == null) {
            log.debug("submitSystemQuoteResp fail due offerSheet invalid");
            return BuyOfferResp.builder().status(BuyOfferResp.STATUS_ENUE.ORDER_INVALID.getValue()).build();
        }
        if (offerSheet.getMinBuyWeight().compareTo(offer.getWeight()) == 1) {
            return BuyOfferResp.builder().status(BuyOfferResp.STATUS_ENUE.WEIGHT_INVALID.getValue()).build();
        }

        //        CargoInfo cargoInfo = new CargoInfo();
        //        cargoInfo.setCommodityId(offer.getCommodityId());
        //        cargoInfo.setDealTime(new Timestamp(System.currentTimeMillis()));
        //        cargoInfo.setPrice(offer.getPrice());
        //        cargoInfo.setUnitWeight(offer.getUnitWeight());
        //        cargoInfo.setWeight(offer.getWeight());
        //        cargoInfo.setUnitWeight(offer.getUnitWeight());
        //        cargoInfo.setCargoId(UUID.randomUUID().toString());
        //        cargoInfoMapper.insertSelective(cargoInfo);
        SaleOrder saleOrder = new SaleOrder();
        BeanUtils.copyProperties(offer, saleOrder);
        // saleOrder.setCargoId(cargoInfo.getCargoId());
        saleOrder.setOrderId(UUID.randomUUID().toString());
        saleOrder.setBuyerId(CookieAuthUtils.getCurrentUser());
        saleOrder.setReceiveLocation(offer.getReceiveLocationId());
        saleOrder.setReceiveStartTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offer.getReceiveStartTime()));
        saleOrder.setReceiveEndTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offer.getReceiveStartTime()));
        if (offerSheet != null) {
            saleOrder.setFromLocation(offerSheet.getLocationId());
        }
        saleOrder.setCommodityPrice(offer.getPrice());
        // todo 订单总价格 需要加上运费
        saleOrder.setOrderPrice(saleOrder.getCommodityPrice().multiply(saleOrder.getCommodityPrice()));
        saleOrder.setStatus(SaleOrder.STATUS_ENUM.UNDER_APPROVAL.getValue());
        //outStackService.buildSystemDefaultOutStackInfo(saleOrder,true);
        int ret = saleOrderMapper.insertSelective(saleOrder);
        if (ret != 1) {
            return BuyOfferResp.builder().status(BuyOfferResp.STATUS_ENUE.SERVER_ERROR.getValue()).build();
        }
        // 构建系统默认的出货记录 todo 待完善
        return BuyOfferResp.builder().status(BuyOfferResp.STATUS_ENUE.SUCCESS.getValue()).
                orderId(saleOrder.getOrderId()).orderStatus(saleOrder.getStatus()).build();
    }

    /**
     * 买家获取下单列表
     *
     * @param getSaleOrderListReq
     * @return
     */
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
                        getSaleOrderListReq.getOrderType(), CookieAuthUtils.getCurrentUser());

        if (getSaleOrderListReq == null) {
            log.debug("getSaleOrderListRespList fail due to get result  empty");
            return Collections.EMPTY_LIST;
        }
        getSaleOrderListResps.stream().forEach(x -> {
            //            if(!x.getStatus().equals(SaleOrder.STATUS_ENUM.CANCLE.getValue())
            //                    && !x.getStatus().equals(SaleOrder.STATUS_ENUM.UNDER_APPROVAL.getValue()) && !x.getStatus().equals(SaleOrder.STATUS_ENUM.UNDER_APPROVAL.getValue())){
            //                x.setCarInfo(orderDeliverService.getSaleCarStatus(x.getOrderId()));
            //                x.setCarTotalNum(x.getCarInfo().size());
            //            }
            Commodity commodity = commodityDao.getCommodityByCommodityId(x.getCommodityId());
            x.setCommodity(commodity);
        });
        log.debug("user is {} getSaleOrderListRespList return data {}", CookieAuthUtils.getCurrentUser(),
                JacksonUtils.obj2String(getSaleOrderListResps));
        return getSaleOrderListResps;

    }

    /**
     * 买家获取指定订单下的货运列表
     *
     * @param saleId
     * @return
     */
    public List<BuyerGelLorryListResp> getSaleList(String saleId) {
        log.debug("getSaleList param saleid is {}", saleId);
        if (Strings.isNullOrEmpty(saleId)) {
            log.debug("getSaleList fail due to param error");
            return Collections.EMPTY_LIST;
        }
        SaleOrder saleOrder = saleOrderMapper.selectBySaleId(saleId);
        if (saleOrder == null || saleOrder.getStatus().equals(SaleOrder.STATUS_ENUM.CANCLE.getValue())) {
            log.debug("getSaleList fail due to param error");
            return Collections.EMPTY_LIST;
        }
        if (!saleOrder.getBuyerId().equals(CookieAuthUtils.getCurrentUser())) {
            log.debug("getSaleList fail due to cur user has no auth ");
            return Collections.EMPTY_LIST;
        }
        //h获取运单ID 关联了order_info 表
        List<String> deliverId = waybillInfoMapper.getSaleOrderDeliverId(saleId);
        if (deliverId == null || deliverId.size() == 0) {
            log.debug("getSaleList fail due to cur sale order has no deliver info ");
            return Collections.EMPTY_LIST;
        }
        List<BuyerGelLorryListResp> orderListResp = orderInfoDao.buyerGetLorryList(deliverId);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(orderListResp)) {
            log.info("getSaleList fail due  delivery query order list is empty sale id is {}", saleId);
            return Lists.newArrayList();
        }
        orderListResp.stream()
                .filter(order -> org.apache.commons.collections.CollectionUtils.isNotEmpty(order.getDriverMobileSet()))
                .forEach(order -> {
                    order.setDriverMobile(order.getDriverMobileSet().iterator().next());
                });
        return orderListResp;
    }

    /**
     * 买家获取指定货运详情
     *
     * @param deliverOrderId 货运ID
     * @return
     */
    public BuyerGelLorryDetailResp getDeliverOrderDetail(String deliverOrderId) {
        log.debug("buyer get deliver {} detail ", deliverOrderId);
        BuyerGelLorryDetailResp buyerGelLorryDetailResp = new BuyerGelLorryDetailResp();
        if (Strings.isNullOrEmpty(deliverOrderId)) {
            log.debug("buyer get deliver detail fail due to param is null");
            buyerGelLorryDetailResp.setOptStatus(BuyerGelLorryDetailResp.STATUS_ENUE.PARAM_ERROR.getValue());
            return buyerGelLorryDetailResp;
        }
        BuyerGelLorryListResp buyerGelLorryListResp = orderInfoDao.buyerGetLorryDetail(deliverOrderId);
        if (buyerGelLorryListResp == null) {
            log.debug("buyer get deliver detail fail due to get deliver order is nulll from order_info table");
            buyerGelLorryDetailResp.setOptStatus(BuyerGelLorryDetailResp.STATUS_ENUE.order_error.getValue());
            return buyerGelLorryDetailResp;
        }
        BeanUtils.copyProperties(buyerGelLorryListResp, buyerGelLorryDetailResp);
        buyerGelLorryDetailResp.setDriverMobile(buyerGelLorryDetailResp.getDriverMobileSet().iterator().next());
        buyerGelLorryDetailResp.setOptStatus(BuyerGelLorryDetailResp.STATUS_ENUE.SUCCESS.getValue());
        log.debug("buyer get deliver detail success return data is {}",
                JacksonUtils.obj2String(buyerGelLorryDetailResp));
        return buyerGelLorryDetailResp;

    }

    public OptResultResp confirmDeliver(String saleId, String deliverId, BigDecimal receiveWeight) {
        if (Strings.isNullOrEmpty(saleId) || Strings.isNullOrEmpty(deliverId) || receiveWeight == null) {
            return OptResultResp.builder().optStatus(OptResultResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        int rst = orderInfoDao.buyerOrderStatusAndSuccWeight(deliverId, receiveWeight,
                OrderInfo.STATUS_ENUE.ORDER_SUCCESS.getValue());
        if (rst != 1) {
            return OptResultResp.builder().optStatus(OptResultResp.STATUS_ENUE.SERVER_ERROR.getValue()).build();
        }
        List<String> deliveId = waybillInfoMapper.getSaleOrderDeliverId(saleId);

        if (deliveId == null || deliveId.size() == 0) {
            log.info("confirmDeliver fail due to get deliver id list is empty by sale id ");
            return OptResultResp.builder().optStatus(OptResultResp.STATUS_ENUE.SERVER_ERROR.getValue()).build();
        }
        List<BuyerGelLorryListResp> gelLorryListResps = orderInfoDao.buyerGetLorryList(deliveId);
        if (gelLorryListResps == null || gelLorryListResps.size() == 0) {
            log.info("confirmDeliver fail due to get deliver list is empty by sale id ");
            return OptResultResp.builder().optStatus(OptResultResp.STATUS_ENUE.SERVER_ERROR.getValue()).build();
        }
        boolean updateSaleStatus = false;
        for (BuyerGelLorryListResp buyerGelLorryListResp : gelLorryListResps) {
            if (!buyerGelLorryListResp.getStatus().equals(OrderInfo.STATUS_ENUE.ORDER_SUCCESS.getValue())) {
                updateSaleStatus = true;
                break;
            }
        }
        if (updateSaleStatus) {
            saleOrderMapper.updateSaleStatus(saleId, SaleOrder.STATUS_ENUM.FINISH.getValue());
        }
        return OptResultResp.builder().optStatus(OptResultResp.STATUS_ENUE.SUCCESS.getValue()).build();

    }

    public OptPurchaseResp editPurchase(SubmitMyOfferReq purchaseInfo) {
        log.debug("edit purchase param is {} ", JacksonUtils.obj2String(purchaseInfo));
        if (purchaseInfo == null) {
            log.debug("edit purchaseInfo fail due to param is invalid");
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        PurchaseInfo beforePu = purchaseInfoMapper.selectByPurchaseId(purchaseInfo.getPurchaseId());
        if (!CookieAuthUtils.getCurrentUser().equals(beforePu.getBuyerId())) {
            log.debug("edit purchaseInfo fail cur user no auth ");
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.USER_ERROR.getValue()).build();
        }

        if (beforePu == null || beforePu.getStatus().equals(PurchaseInfo.STATUS_ENUM.CANCLE.getValue())) {
            log.debug("edit purchaseInfo fail purchase invalid ");
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.PURCHASE_INVALID.getValue()).build();
        }
        if (beforePu.getStatus().equals(PurchaseInfo.STATUS_ENUM.DEAL.getValue())) {
            log.debug("edit purchaseInfo fail cur status is deal");
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.DEAL_ERROR.getValue()).build();
        }
        PurchaseInfo purchaseInfoUp = new PurchaseInfo();
        BeanUtils.copyProperties(purchaseInfo, purchaseInfoUp);
        if (!Strings.isNullOrEmpty(purchaseInfo.getReceiveStartTime())) {
            purchaseInfoUp.setReceiveStartTime(
                    TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, purchaseInfo.getReceiveStartTime()));

        }
        if (!Strings.isNullOrEmpty(purchaseInfo.getReceiveEndTime())) {
            purchaseInfoUp.setReceiveEndTime(
                    TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, purchaseInfo.getReceiveEndTime()));
        }
        int res = purchaseInfoMapper.updateByPrimaryKeySelective(purchaseInfoUp);
        if (res != 1) {
            log.debug("edit purchaseInfo fail due to update db fail");
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.SERVER_ERROR.getValue()).build();
        }
        PurchaseInfo beforePhu = purchaseInfoMapper.selectByPurchaseId(purchaseInfo.getPurchaseId());
        return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.SUCCESS.getValue())
                .purchaseId(purchaseInfo.getPurchaseId()).purchaseStatus(beforePhu.getStatus()).build();
    }

    public GetPurchaseBargainResp getPurcahseBargain(String purchaseId) {
        PurchaseInfo purchaseInfo = purchaseInfoMapper.selectByPurchaseId(purchaseId);
        if (purchaseInfo == null) {
            return GetPurchaseBargainResp.builder()
                    .optStatus(GetPurchaseBargainResp.STATUS_ENUE.purcahse_INVALID.getValue()).build();
        }
        List<GetPurchaseBargainResp.priceDetail> details = new ArrayList<>(PURCHASE_BARGAIN.size());
        PURCHASE_BARGAIN.stream().forEach(x -> {
            GetPurchaseBargainResp.priceDetail priceDetail = new GetPurchaseBargainResp.priceDetail();
            priceDetail.setBeforeOrderPrice(purchaseInfo.getPrice().multiply(purchaseInfo.getWeight()));
            priceDetail.setBeforeUnitPrice(purchaseInfo.getPrice());
            priceDetail.setIncrease(x);
            BigDecimal curUnit = purchaseInfo.getPrice().add(x);
            priceDetail.setAfterUnitPrice(curUnit);
            priceDetail.setAfterOrderPrice(curUnit.multiply(purchaseInfo.getWeight()));
            details.add(priceDetail);
        });
        return GetPurchaseBargainResp.builder().optStatus(GetPurchaseBargainResp.STATUS_ENUE.SUCCESS.getValue())
                .priceDetail(details).build();
    }

    public OptPurchaseResp updatePurchasePrice(UpdatePurcahsePriceReq updatePurcahsePriceReq) {
        //        if(updatePurcahsePriceReq.getIncrease()==null){
        //            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        //        }
        PurchaseInfo purchaseInfo = purchaseInfoMapper.selectByPurchaseId(updatePurcahsePriceReq.getPurchaseId());
        if (purchaseInfo == null) {
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.PURCHASE_INVALID.getValue()).build();

        }
        if (!purchaseInfo.getBuyerId().equals(CookieAuthUtils.getCurrentUser())) {
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.USER_ERROR.getValue()).build();
        }
        //todo 这儿要在数据库加一个字段 表明总价格
        purchaseInfo.setPrice(updatePurcahsePriceReq.getAfterOrderPrice().divide(purchaseInfo.getWeight()));
        int res = purchaseInfoMapper.updateByPrimaryKeySelective(purchaseInfo);
        if (res != 1) {
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.SERVER_ERROR.getValue()).build();
        }
        return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.SUCCESS.getValue())
                .purchaseStatus(purchaseInfo.getStatus()).purchaseId(updatePurcahsePriceReq.getPurchaseId()).build();
    }

    public OptPurchaseResp updatePurchase(String purchaseId, Integer status) {

        if (Strings.isNullOrEmpty(purchaseId) || status == null) {
            log.debug("update purchaseInfo status fail due to param is invalid");
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        PurchaseInfo beforePu = purchaseInfoMapper.selectByPurchaseId(purchaseId);
        if (!CookieAuthUtils.getCurrentUser().equals(beforePu.getBuyerId())) {
            log.debug("update purchaseInfo fail cur user no auth ");
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.USER_ERROR.getValue()).build();
        }
        if (beforePu == null || beforePu.getStatus().equals(PurchaseInfo.STATUS_ENUM.CANCLE.getValue())) {
            log.debug("edit purchaseInfo fail purchase invalid ");
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.PURCHASE_INVALID.getValue()).build();
        }
        if (beforePu.getStatus().equals(PurchaseInfo.STATUS_ENUM.DEAL.getValue())) {
            log.debug("edit purchaseInfo fail cur status is deal");
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.DEAL_ERROR.getValue()).build();
        }
        int res = purchaseInfoMapper.updateStatus(purchaseId, status);
        if (res != 1) {
            log.debug("edit purchaseInfo fail due to update db fail");
            return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.SERVER_ERROR.getValue()).build();
        }
        return OptPurchaseResp.builder().optStatus(OptPurchaseResp.STATUS_ENUE.SUCCESS.getValue())
                .purchaseStatus(status).purchaseId(purchaseId).build();
    }

    /**
     * 查询买家下面的求购总数目
     *
     * @return
     */
    public GetPurchaseNumResp getPurchaseNum() {
        int num = purchaseInfoMapper.getPurchaseNum(CookieAuthUtils.getCurrentUser());
        GetPurchaseNumResp getPurchaseNumResp = new GetPurchaseNumResp();
        getPurchaseNumResp.setNum(new Integer(num));
        return getPurchaseNumResp;

    }

    public GetPurchaseDetailResp getPurchaseDetailResp(String purchaseId) {
        log.debug("get {} purchase detail ", purchaseId);
        GetPurchaseDetailResp getPurchaseDetailResp = new GetPurchaseDetailResp();
        if (Strings.isNullOrEmpty(purchaseId)) {
            getPurchaseDetailResp.setOptStatus(GetPurchaseDetailResp.STATUS_ENUE.PARAM_ERROE.getValue());
            return getPurchaseDetailResp;
        }
        PurchaseInfo purchaseInfo = purchaseInfoMapper.selectByPurchaseId(purchaseId);
        if (purchaseInfo == null) {
            log.debug("get purchase detail fail due to can not get purchase {} form purchase_info ", purchaseId);
            getPurchaseDetailResp.setOptStatus(GetPurchaseDetailResp.STATUS_ENUE.PURCHASE_INVALID.getValue());
            return getPurchaseDetailResp;
        }
        if (!purchaseInfo.getBuyerId().equals(CookieAuthUtils.getCurrentUser())) {
            log.debug("get {} purchase detail fail due no auth,cur user is {}, order user is {}", purchaseId,
                    CookieAuthUtils.getCurrentUser(), purchaseInfo.getBuyerId());
            getPurchaseDetailResp.setOptStatus(GetPurchaseDetailResp.STATUS_ENUE.NO_AUTH.getValue());
            return getPurchaseDetailResp;
        }
        BeanUtils.copyProperties(purchaseInfo, getPurchaseDetailResp);
        getPurchaseDetailResp.setCommodityPrice(purchaseInfo.getPrice());
        getPurchaseDetailResp.setCommodity(commodityDao.buyerGetCommodity(purchaseInfo.getCommodityId()));
        getPurchaseDetailResp.setOrderPrice(purchaseInfo.getPrice().multiply(purchaseInfo.getWeight()));
        getPurchaseDetailResp.setReceiveStartTime(
                TimeStampUtil.timeStampConvertString(TIME_FORMAT, purchaseInfo.getReceiveStartTime()));
        getPurchaseDetailResp
                .setReceiveEndTime(TimeStampUtil.timeStampConvertString(TIME_FORMAT, purchaseInfo.getReceiveEndTime()));
        getPurchaseDetailResp.setOptStatus(GetPurchaseDetailResp.STATUS_ENUE.SUCCESS.getValue());
        getPurchaseDetailResp.setSaleId(saleOrderMapper.getSaleIdByPurchaseId(purchaseId));
        LocationInfo locationInfo = locationInfoMapper.selectByLocationId(purchaseInfo.getReceiveLocationId());
        if (locationInfo == null) {
            log.error(
                    "get purchase detail but can not get receive location, the receive location id is {} in purchase_info ",
                    purchaseInfo.getReceiveLocationId());
            getPurchaseDetailResp.setLocationId("");
            getPurchaseDetailResp.setLocationArea("");
            getPurchaseDetailResp.setLocationDetail("");
        } else {
            getPurchaseDetailResp.setLocationId(locationInfo.getLocationId());
            getPurchaseDetailResp.setLocationArea(locationInfo.getLocationArea());
            getPurchaseDetailResp.setLocationDetail(locationInfo.getLocationDetail());
        }

        getPurchaseDetailResp.setLocation(locationInfo.getLocation());
        return getPurchaseDetailResp;

    }

    /**
     * 买家获取我的求购list
     *
     * @param getPurchaseListReq
     * @return
     */
    public List<GetPurchaseListResp> getPurchaseListRespList(GetPurchaseListReq getPurchaseListReq) {
        if (getPurchaseListReq == null) {
            log.debug("getSaleOrderListRespList fail due to getSaleOrderListReq empty");
            return Collections.EMPTY_LIST;
        }
        List<PurchaseInfo> purchaseInfos = purchaseInfoMapper
                .getPurcharseList(CookieAuthUtils.getCurrentUser(), getPurchaseListReq.getPurchaseId(),
                        Optional.ofNullable(getPurchaseListReq.getPgSize()).orElse(10));

        if (purchaseInfos == null) {
            log.debug("cur user {} has no purchase order ", CookieAuthUtils.getCurrentUser());
            return Collections.EMPTY_LIST;
        }
        List<GetPurchaseListResp> getPurchaseListResps = new ArrayList<>();
        purchaseInfos.stream().forEach(x -> {
            GetPurchaseListResp getPurchaseListResp = new GetPurchaseListResp();
            BeanUtils.copyProperties(x, getPurchaseListResp);
            getPurchaseListResp.setCommodityPrice(x.getPrice());
            getPurchaseListResp.setCommodity(commodityDao.buyerGetCommodity(x.getCommodityId()));
            getPurchaseListResp.setOrderPrice(getPurchaseListResp.getCommodityPrice().multiply(x.getWeight()));
            getPurchaseListResp
                    .setReceiveStartTime(TimeStampUtil.timeStampConvertString(TIME_FORMAT, x.getReceiveStartTime()));
            getPurchaseListResp
                    .setReceiveEndTime(TimeStampUtil.timeStampConvertString(TIME_FORMAT, x.getReceiveEndTime()));
            getPurchaseListResp.setSaleId(saleOrderMapper.getSaleIdByPurchaseId(x.getPurchaseId()));
            getPurchaseListResps.add(getPurchaseListResp);
        });
        return getPurchaseListResps;
    }

    private boolean checkOffet(SystemOfferReq offerReq) {
        log.debug("checkOffet offerReq is [{}]", JacksonUtils.obj2String(offerReq));
        if (offerReq == null) {
            return false;
        }
        if (Strings.isNullOrEmpty(offerReq.getCommodityId()) || Strings.isNullOrEmpty(offerReq.getContactUserName())
                || Strings.isNullOrEmpty(offerReq.getMobile()) || Strings.isNullOrEmpty(offerReq.getReceiveLocationId())
                || null == offerReq.getWeight() || null == offerReq.getPrice() || Strings
                .isNullOrEmpty(offerReq.getReceiveStartTime()) || Strings.isNullOrEmpty(offerReq.getReceiveEndTime())) {
            return false;
        }
        return true;

    }

    public GetSaleDetailResp getSaleOrderDetail(String saleId) {
        SaleOrder saleOrder = saleOrderMapper.selectBySaleId(saleId);
        GetSaleDetailResp getSaleDetailResp = new GetSaleDetailResp();
        if (saleOrder == null) {
            log.info("get sale detai fail due to order invalid");
            getSaleDetailResp.setStatus(GetSaleDetailResp.STATUS_ENUE.purcahse_INVALID.getValue());
            return getSaleDetailResp;
        }
        if (!saleOrder.getBuyerId().equals(CookieAuthUtils.getCurrentUser())) {
            log.info("get sale detai fail due to cur user has no auth ");
            getSaleDetailResp.setStatus(GetSaleDetailResp.STATUS_ENUE.USER_ERROR.getValue());
            return getSaleDetailResp;

        }
        BeanUtils.copyProperties(saleOrder, getSaleDetailResp);
        //        if(!saleOrder.getStatus().equals(SaleOrder.STATUS_ENUM.CANCLE.getValue()) ||
        //                !saleOrder.getStatus().equals(SaleOrder.STATUS_ENUM.UNDER_APPROVAL.getValue())
        //                ||!saleOrder.getStatus().equals(SaleOrder.STATUS_ENUM.UNDER_DELIVER.getValue())) {
        //            log.info("sale under deliver so add car info to order");
        //            List<DriverInfoResp> cars = orderDeliverService.getSaleCarStatus(saleId);
        //            getSaleDetailResp.setCarInfo(cars);
        //            getSaleDetailResp.setCarTotalNum(getSaleDetailResp.getCarInfo().size());
        //        }

        getSaleDetailResp.setOrderTime(TimeStampUtil.timeStampConvertString(TIME_FORMAT, saleOrder.getOrderTime()));
        getSaleDetailResp.setCommodity(commodityDao.getCommodityByCommodityId(getSaleDetailResp.getCommodityId()));
        getSaleDetailResp.setContactUserName(saleOrder.getContactUserName());
        getSaleDetailResp.setContactMobile(saleOrder.getMobile());
        getSaleDetailResp.setReceiveStartTime(
                TimeStampUtil.timeStampConvertString(TIME_FORMAT, saleOrder.getReceiveStartTime()));
        getSaleDetailResp
                .setReceiveEndTime(TimeStampUtil.timeStampConvertString(TIME_FORMAT, saleOrder.getReceiveEndTime()));

        LocationInfo locationInfo = locationInfoMapper.selectByLocationId(saleOrder.getReceiveLocation());
        if (locationInfo == null) {
            log.error("buyer get order detail,can't get receive location from location_info location id is {} ",
                    saleOrder.getReceiveLocation());
            getSaleDetailResp.setReceiveLocation("");
            getSaleDetailResp.setReceiveLocationId("");
        } else {
            getSaleDetailResp.setReceiveLocation(locationInfo.getLocation());
            getSaleDetailResp.setReceiveLocationId(saleOrder.getReceiveLocation());
        }
        getSaleDetailResp.setOptResult(GetSaleDetailResp.STATUS_ENUE.SUCCESS.getValue());
        log.debug("get sale order detail success return data is  {}", JacksonUtils.obj2String(getSaleDetailResp));
        return getSaleDetailResp;
    }
}
