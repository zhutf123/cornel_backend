package com.demai.cornel.demeManager.service;

import com.demai.cornel.dao.CommodityDao;
import com.demai.cornel.demeManager.model.ShippProcess;
import com.demai.cornel.demeManager.model.SpecialQuote;
import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.model.ReviewModel;
import com.demai.cornel.purcharse.dao.BuyerInfoMapper;
import com.demai.cornel.purcharse.dao.OfferSheetMapper;
import com.demai.cornel.purcharse.dao.SaleOrderMapper;
import com.demai.cornel.purcharse.dao.SpecialSaleInfoMapper;
import com.demai.cornel.purcharse.model.OfferSheet;
import com.demai.cornel.purcharse.model.SaleOrder;
import com.demai.cornel.purcharse.model.SpecialSaleInfo;
import com.demai.cornel.purcharse.model.TransportType;
import com.demai.cornel.purcharse.vo.GetSystemOfferResp;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.DateFormatUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.util.TimeStampUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Author binz.zhang
 * @Date: 2020-03-30    15:18
 */
@Slf4j @Service public class AdminPurchaseCornService {
    @Resource private OfferSheetMapper offerSheetMapper;
    @Resource private BuyerInfoMapper buyerInfoMapper;
    @Resource private SpecialSaleInfoMapper specialSaleInfoMapper;
    @Resource private SaleOrderMapper saleOrderMapper;
    @Resource private CommodityDao commodityDao;

    /**
     * 管理员获取系统的报价list
     *
     * @return
     */
    public List<AdminGetSysOffResp> adminGetSysOffList() {
        List<AdminGetSysOffResp> getSystemOfferReqs = offerSheetMapper.adminGetSysOfferSheet();
        return CollectionUtils.isEmpty(getSystemOfferReqs) ? Lists.newArrayList() : getSystemOfferReqs;
    }

    /**
     * 管理员获取买家list
     *
     * @param req
     * @return
     */
    public List<AdminGetBuyerResp> adminGetBuyerList(AdminGetBuyerReq req) {
        Preconditions.checkNotNull(req);
        List<AdminGetBuyerResp> buyers = buyerInfoMapper
                .adminGetBuyerList(Optional.ofNullable(req.getOffset()).orElse(0),
                        Optional.ofNullable(req.getPgSize()).orElse(10));

        return CollectionUtils.isEmpty(buyers) ? Lists.newArrayList() : buyers;
    }

    /**
     * 管理员修改买家的价格 可以设置全局的报价 也可以修改针对特定买家的价格
     *
     * @param req
     * @return
     */
    public AdminOperResp adminEditSysOfferQuote(AdminEditSysOfferReq req) {
        if (req == null) {
            return AdminOperResp.builder().optStatus(AdminOperResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        for (AdminEditSysOfferReq.quoteInfo x : req.getQuoteInfos()) {
            if (x.getSystemFlag() == null || !x.getSystemFlag().equals(1)) {
                if (!editSpecialOfferSheet(x)) {
                    return AdminOperResp.builder().optStatus(AdminOperResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
                }
            } else {
                if (!editSystemOfferSheet(x)) {
                    return AdminOperResp.builder().optStatus(AdminOperResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
                }
            }
        }
        return AdminOperResp.builder().optStatus(AdminOperResp.STATUS_ENUE.SUCCESS.getValue()).build();

    }

    public List<AdminGetTowerQuLiResp> getBuyerQuoteList(String buyerId) {
        List<SpecialSaleInfo> specialQuote = specialSaleInfoMapper.selectSpecilaByUserId(buyerId);
        if (CollectionUtils.isEmpty(specialQuote)) {
            log.debug("cur user {}  get getBuyerQuoteList list empty", CookieAuthUtils.getCurrentUser());
            return Collections.EMPTY_LIST;
        }
        List<AdminGetTowerQuLiResp> resp = new ArrayList<>();
        specialQuote.stream().forEach(x -> {
            Commodity commodity = commodityDao.getCommodityByCommodityId(x.getCommodityId());
            AdminGetTowerQuLiResp adminGetTowerQuLiResp = new AdminGetTowerQuLiResp();
            BeanUtils.copyProperties(x, adminGetTowerQuLiResp);
            adminGetTowerQuLiResp.setCommodityName(commodity.getName());
            adminGetTowerQuLiResp.setQuote(x.getPrice());
            resp.add(adminGetTowerQuLiResp);
        });
        return resp;
    }

    /**
     * 校验系统报价的参数
     *
     * @param quoteInfo
     * @return
     */
    private boolean checkQuoteParam(AdminEditSysOfferReq.quoteInfo quoteInfo) {
        log.debug("edit system offer sheet quote  param is [{}]", JacksonUtils.obj2String(quoteInfo));
        if (quoteInfo == null) {
            log.error("edit system offer sheet quote fail due to param NULL ");
            return false;
        }
        if (Strings.isNullOrEmpty(quoteInfo.getCommodityId()) || quoteInfo.getSelfQuote() == null) {
            return false;
        }
        if (quoteInfo.getSystemFlag() != null && quoteInfo.getSystemFlag().equals(0) && Strings
                .isNullOrEmpty(quoteInfo.getUserId())) {
            return false;
        }
        return true;
    }

    /**
     * 修改系统报价
     *
     * @param quoteInfo
     * @return
     */
    @Transactional public boolean editSystemOfferSheet(AdminEditSysOfferReq.quoteInfo quoteInfo) {
        if(quoteInfo==null || quoteInfo.getSelfQuote()==null){
            return true;
        }
        if (!checkQuoteParam(quoteInfo)) {
            return false;
        }
        OfferSheet oldOffer = offerSheetMapper.selectByCommodityId(quoteInfo.getCommodityId());
        if (oldOffer == null) {
            log.warn("edit system offer sheet fail due to old sheet not found");
            return false;
        }
        OfferSheet newOff = new OfferSheet();
        BeanUtils.copyProperties(oldOffer, newOff);
        newOff.setOfferId(UUID.randomUUID().toString());
        newOff.setCreateTime(new Timestamp(System.currentTimeMillis()));
        newOff.setPrice(quoteInfo.getSelfQuote());
        newOff.setStatus((short) 1);
        int res = offerSheetMapper.updateOfferStatusByCommodityId(newOff.getCommodityId());
        if (res == 0) {
            log.warn("edit system offer sheet fail due to update old sheet fail ");
            return false;
        }
        newOff.setId(null);
        int resInser = offerSheetMapper.insertSelective(newOff);
        if (resInser == 0) {
            log.warn("edit system offer sheet fail due to insert new sheet fail ");
            return false;
        }
        return true;
    }

    /**
     * 修改针对个人的报价
     */
    @Transactional public boolean editSpecialOfferSheet(AdminEditSysOfferReq.quoteInfo quoteInfo) {
        if(quoteInfo==null || quoteInfo.getSelfQuote()==null){
            return true;
        }

        if (!checkQuoteParam(quoteInfo)) {
            return false;
        }
        SpecialSaleInfo oldOffer = specialSaleInfoMapper
                .selectSpecilaByCommodityIdAndUserId(quoteInfo.getCommodityId(), quoteInfo.getUserId());
        SpecialSaleInfo newSpe = new SpecialSaleInfo();
        boolean alreadyExist = false;
        if (oldOffer == null) {
            OfferSheet oldOfferSys = offerSheetMapper.selectByCommodityId(quoteInfo.getCommodityId());
            if (oldOfferSys == null) {
                log.error("editSpecialOfferSheet fail due to quote not find");
                return false;
            }
            BeanUtils.copyProperties(oldOfferSys, newSpe);
        } else {
            BeanUtils.copyProperties(oldOffer, newSpe);
            alreadyExist = true;
        }
        newSpe.setCreateTime(new Timestamp(System.currentTimeMillis()));
        newSpe.setReviewUser(CookieAuthUtils.getCurrentUser());
        newSpe.setId(null);
        newSpe.setPrice(quoteInfo.getSelfQuote());
        newSpe.setTargetUserId(quoteInfo.getUserId());
        newSpe.setStatus(1);
        if (alreadyExist) {
            int res = specialSaleInfoMapper
                    .updateOfferStatusByCommodityIdAndUserId(newSpe.getCommodityId(), newSpe.getTargetUserId());
            if (res == 0) {
                log.warn("edit special offer sheet fail due to update old sheet fail ");
                return false;
            }
        }
        int resInser = specialSaleInfoMapper.insertSelective(newSpe);
        if (resInser != 1) {
            log.warn("edit special offer sheet fail due to insert new sheet fail ");
            return false;
        }
        return true;
    }

    public List<ReviewModel> getReviewErrOpt() {
        List<ReviewModel> reviewModels = new ArrayList<>();
        Arrays.stream(ReviewModel.SALE_ORDER_ERR.values()).forEach(x -> {
            ReviewModel reviewModel = new ReviewModel();
            reviewModel.setErrCode(x.getValue());
            reviewModel.setDesc(x.getExpr());
            reviewModels.add(reviewModel);
        });
        return reviewModels;
    }

    public AdminReviewSaleResp reviewPay(AdminReviewPayReq adminReviewPayReq) {
        if (adminReviewPayReq == null || adminReviewPayReq.getActualPay() == null || Strings
                .isNullOrEmpty(adminReviewPayReq.getOrderId()) || Strings
                .isNullOrEmpty(adminReviewPayReq.getPayTime())) {
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.PARAM_ERROR.getValue())
                    .build();
        }
        SaleOrder saleOrder = saleOrderMapper.selectBySaleId(adminReviewPayReq.getOrderId());
        if (saleOrder == null) {
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.ORDER_INVALID.getValue())
                    .build();
        }
        if (!saleOrder.getViewStatus().equals(SaleOrder.STATUS_VIEW.UNDER_PAY.getValue())) {
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.ORDER_UN_CHANGE.getValue())
                    .build();
        }

        SaleOrder saleOrderNew = new SaleOrder();
        saleOrderNew.setPaymentStatus(1);
        saleOrderNew.setOrderId(saleOrder.getOrderId());
        saleOrderNew.setViewStatus(SaleOrder.STATUS_VIEW.FINISH.getValue());
        saleOrderNew.setStatus(SaleOrder.STATUS_ENUM.FINISH.getValue());
        saleOrderNew.setPayReview(CookieAuthUtils.getCurrentUser());
        saleOrderNew.setActualPay(adminReviewPayReq.getActualPay());
        saleOrderNew.setPayTime(TimeStampUtil.stringConvertTimeStamp(DateFormatUtils.ISO_DATE_PATTERN, adminReviewPayReq.getPayTime()));
        saleOrderNew.setExInfo(Strings.isNullOrEmpty(adminReviewPayReq.getExInfo())?"":adminReviewPayReq.getExInfo());
        int res = saleOrderMapper.updateByPrimaryKeySelective(saleOrderNew);
        if (res != 1) {
            return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.SERVER_ERR.getValue())
                    .build();
        }
        return AdminReviewSaleResp.builder().optStatus(AdminReviewSaleResp.STATUS_ENUE.SUCCESS.getValue()).build();
    }
    public List<ShippProcess> getShippWay(){
        List<ShippProcess>shippProcesses = new LinkedList<>();
        Arrays.stream(ShippProcess.TYPE.values()).forEach(x->{
            shippProcesses.add(new ShippProcess(x.getExpr(),x.getValue()));
        });
        return shippProcesses;
    }


}
