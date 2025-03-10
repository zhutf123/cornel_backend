package com.demai.cornel.service;

import com.demai.cornel.dao.*;
import com.demai.cornel.demeManager.dao.SpecialQuoteMapper;
import com.demai.cornel.model.DryTower;
import com.demai.cornel.model.LoanInfo;
import com.demai.cornel.model.QuoteInfo;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.util.TimeStampUtil;
import com.demai.cornel.vo.quota.*;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.hp.gagawa.java.elements.B;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.postgresql.jdbc.TimestampUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.UUID;

import static com.demai.cornel.service.QuoteService.DATE_TIME_FORMAT;
import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @Author binz.zhang
 * @Date: 2020-03-16    17:01
 */
@Slf4j @Service public class QuoteServiceV3 {
    @Resource private QuoteInfoDao quoteInfoDao;
    @Resource private UserInfoDao userInfoDao;
    @Resource private DryTowerDao dryTowerDao;
    @Resource private SystemQuoteDao systemQuoteDao;
    @Resource private CommodityDao commodityDao;
    @Resource private CommodityService commodityService;
    @Resource private SpecialQuoteMapper specialQuoteMapper;
    @Resource private LoanInfoMapper loanInfoMapper;
    @Resource private ImgService imgService;
    @Resource private QuoteService quoteService;

    public OfferQuoteResp offerSystemQuoteV2(SystemQuoteV2Req offerQuoteReq) {
        OfferQuoteResp offerQuoteResp = new OfferQuoteResp();
        Preconditions.checkNotNull(offerQuoteReq);
        log.debug("dry tower quote info is [{}]", JacksonUtils.obj2String(offerQuoteReq));
        if (!checkQuote(offerQuoteReq)) {
            log.info("dry tower quote info fail due to param illegal");
            offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue());
            return offerQuoteResp;
        }
        DryTower dryTower = dryTowerDao.selectByTowerId(offerQuoteReq.getTowerId());
        if (dryTower == null) {
            log.info("dry tower quote info fail due to dry tower illegal");
            offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.DRY_TOWER_ERROR.getValue());
            return offerQuoteResp;
        }
        if (offerQuoteReq.getCargoStatus().equals(QuoteInfo.CARGO_STATUS.spot.getValue())) {
            offerQuoteReq.setWarehouseTime(
                    TimeStampUtil.timeStampConvertString("yyyy-MM-dd", new Timestamp(System.currentTimeMillis())));
        }
        QuoteInfo quoteInfo = new QuoteInfo();
        BeanUtils.copyProperties(offerQuoteReq, quoteInfo);
        String userId = CookieAuthUtils.getCurrentUser();
        quoteInfo.setLocation(dryTower.getLocation());
        quoteInfo.setUserId(userId);
        // if (offerQuoteReq.getLoanPrice() != null) {
        LoanInfo loanInfo = new LoanInfo();
        loanInfo.setApplyUserId(CookieAuthUtils.getCurrentUser());
        loanInfo.setApplyTime(new Timestamp(System.currentTimeMillis()));
        loanInfo.setLoanId(UUID.randomUUID().toString());
        loanInfo.setPrice(offerQuoteReq.getLoanPrice() == null ? new BigDecimal(0) : offerQuoteReq.getLoanPrice());
        int res = loanInfoMapper.insertSelective(loanInfo);
        if (res != 1) {
            log.error("insert loan info into db error ");
            offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.SERVER_ERROR.getValue());
            return offerQuoteResp;
        }
        quoteInfo.setLoanId(Sets.newHashSet(loanInfo.getLoanId()));
        // }
        if (Strings.isNullOrEmpty(offerQuoteReq.getMobile())) {
            UserInfo userInfo = userInfoDao.getUserInfoByUserId(CookieAuthUtils.getCurrentUser());
            if (!CollectionUtils.isEmpty(userInfo.getMobile())) {
                quoteInfo.setMobile(userInfo.getMobile().iterator().next());
            }
        }
        Timestamp warehouseTime = quoteService.getWarehouseTime(offerQuoteReq.getWarehouseTime());
        quoteInfo.setSystemFlag(QuoteInfo.SYSTEM_STATUS.SYSTEM.getValue());
        quoteInfo.setUserName(userInfoDao.getUserNameByUserId(userId));
        quoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.UNDER_SER_REVIEW.getValue());
        quoteInfo.setQuoteId(UUID.randomUUID().toString());
        quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(DATE_TIME_FORMAT, offerQuoteReq.getStartTime()));
        quoteInfo.setEndTime(TimeStampUtil.stringConvertTimeStamp(DATE_TIME_FORMAT, offerQuoteReq.getEndTime()));
        quoteInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        quoteInfo.setWarehouseTime(warehouseTime);
        quoteInfo.setWetPrice(offerQuoteReq.getWetPrice());
        int resInserQu = quoteInfoDao.insertSelective(quoteInfo);
        if (resInserQu != 1) {
            log.error("insert quote info  into db error ");
            offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.SERVER_ERROR.getValue());
            return offerQuoteResp;
        }
        imgService.saveQuoteImgs(offerQuoteReq.getImgs(), quoteInfo.getQuoteId());
        offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.SUCCESS.getValue());
        offerQuoteResp.setQuoteStatus(quoteInfo.getStatus());
        offerQuoteResp.setQuoteId(quoteInfo.getQuoteId());
        return offerQuoteResp;
    }

    public OfferQuoteResp editSystemQuote(SystemQuoteV2Req offerQuoteReq) {
        OfferQuoteResp offerQuoteResp = new OfferQuoteResp();
        Preconditions.checkNotNull(offerQuoteReq);
        log.debug("dry tower update quote info is [{}]", JacksonUtils.obj2String(offerQuoteReq));
        if (!checkQuote(offerQuoteReq)) {
            log.info("dry tower quote info fail due to param illegal");
            offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue());
            return offerQuoteResp;
        }

        QuoteInfo oldQuote = quoteInfoDao.selectByPrimaryKey(offerQuoteReq.getQuoteId());
        if (oldQuote == null) {
            log.info("edit  quote info fail due to get old quote info is null");
            offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.ORDER_INVALD.getValue());
            return offerQuoteResp;
        }
        if (!oldQuote.getStatus().equals(QuoteInfo.QUOTE_TATUS.UNDER_SER_REVIEW.getValue())) {
            log.info("edit quote info fail due to  quote STATUS can't update ");
            offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.ORDER_STATUS_INVALD.getValue());
            return offerQuoteResp;
        }
        if (!oldQuote.getUserId().equals(CookieAuthUtils.getCurrentUser())) {
            log.info("edit  quote info fail due to  cur user has no auth ");
            offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.USER_ERR.getValue());
            return offerQuoteResp;
        }

        QuoteInfo quoteInfo = new QuoteInfo();
        BeanUtils.copyProperties(offerQuoteReq, quoteInfo);

        if (Strings.isNullOrEmpty(offerQuoteReq.getTowerId())) {
            DryTower dryTower = dryTowerDao.selectByTowerId(offerQuoteReq.getTowerId());
            if (dryTower == null) {
                log.info("dry tower quote info fail due to dry tower illegal");
                offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.DRY_TOWER_ERROR.getValue());
                return offerQuoteResp;
            }
            quoteInfo.setLocation(dryTower.getLocation());
        }

        if (Strings.isNullOrEmpty(offerQuoteReq.getMobile())) {
            UserInfo userInfo = userInfoDao.getUserInfoByUserId(CookieAuthUtils.getCurrentUser());
            if (!CollectionUtils.isEmpty(userInfo.getMobile())) {
                quoteInfo.setMobile(userInfo.getMobile().iterator().next());
            }
        }
        if (!updateLoanInfo(oldQuote, offerQuoteReq.getLoanPrice(), quoteInfo)) {
            log.info("update quote loan info fail ");
            offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.DRY_TOWER_ERROR.getValue());
            return offerQuoteResp;

        }
        if (offerQuoteReq.getImgs() != null && offerQuoteReq.getImgs().size() > 0) {
            imgService.updateQuoteImg(offerQuoteReq.getImgs(), offerQuoteReq.getQuoteId());
        }
        quoteInfo.setQuoteId(oldQuote.getQuoteId());
        quoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.UNDER_SER_REVIEW.getValue());
        quoteInfo.setSystemFlag(QuoteInfo.SYSTEM_STATUS.SYSTEM.getValue());
        quoteInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (!Strings.isNullOrEmpty(offerQuoteReq.getStartTime())) {
            quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(DATE_TIME_FORMAT, offerQuoteReq.getStartTime()));
        }
        if (!Strings.isNullOrEmpty(offerQuoteReq.getEndTime())) {
            quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(DATE_TIME_FORMAT, offerQuoteReq.getEndTime()));
        }
        if (!Strings.isNullOrEmpty(offerQuoteReq.getWarehouseTime())) {
            quoteInfo.setWarehouseTime(quoteService.getWarehouseTime(offerQuoteReq.getWarehouseTime()));
        }
        int res = 0;
        try {
            res = quoteInfoDao.updateByPrimaryKeySelective(quoteInfo);
        } catch (Exception e) {
            log.error("update quote order fail du to update db fail", 2);
        }
        if (res != 1) {
            log.error("update quote order fail du to update db fail");
            offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.SERVER_ERROR.getValue());
            return offerQuoteResp;
        }
        offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.SUCCESS.getValue());
        offerQuoteResp.setQuoteStatus(quoteInfo.getStatus());
        offerQuoteResp.setQuoteId(quoteInfo.getQuoteId());
        return offerQuoteResp;
    }

    public GetQuotePriceResp getQuotePrice(GetQuotePriceRep req) throws ParseException {
        if (req == null || Strings.isNullOrEmpty(req.getCommodityId()) || Strings.isNullOrEmpty(req.getTime())) {
            log.debug("get quote price err due to param err");
            return GetQuotePriceResp.builder().optResult(GetQuotePriceResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        GetQuotePriceResp getQuotePriceResp = new GetQuotePriceResp();
        Timestamp spNeT = specialQuoteMapper
                .getNearestCommodityPriceTime(CookieAuthUtils.getCurrentUser(), req.getCommodityId());
        Timestamp syNeT = systemQuoteDao.getNearestCommodityPriceTime(req.getCommodityId());

        if (spNeT == null && syNeT == null) {
            log.debug("GetQuotePriceResp get fail due to spNeT and syNeT all is null");
            return GetQuotePriceResp.builder().optResult(GetQuotePriceResp.STATUS_ENUE.COMMODITY_ERROR.getValue())
                    .build();
        }
        if (spNeT == null) {
            getQuotePriceResp.setStartTime(TimeStampUtil.timeStampConvertString("yyyy-MM-dd", syNeT));
        } else {
            Timestamp nearTime = spNeT.before(syNeT) ? spNeT : syNeT;
            getQuotePriceResp.setStartTime(TimeStampUtil.timeStampConvertString("yyyy-MM-dd", nearTime));
        }

        Date time = new Date(System.currentTimeMillis());
        BigDecimal quote = specialQuoteMapper
                .getNearestCommodityPrice(CookieAuthUtils.getCurrentUser(), req.getCommodityId(), time);
        if (quote != null) {
            getQuotePriceResp.setQuote(quote);
        } else {
            quote = systemQuoteDao.getNearestCommodityPrice(req.getCommodityId(), time);
            if (quote != null) {
                getQuotePriceResp.setQuote(quote);
            }
        }
        if (getQuotePriceResp.getQuote() == null) {
            getQuotePriceResp.setOptResult(GetQuotePriceResp.STATUS_ENUE.TIME_ERR.getValue());
            return getQuotePriceResp;
        }
        getQuotePriceResp.setOptResult(GetQuotePriceResp.STATUS_ENUE.SUCCESS.getValue());
        return getQuotePriceResp;

    }

    public GetDryWetRadioResp getDryWetRadio() {
        return new GetDryWetRadioResp();
    }

    public CalculateDryWeiResp calculateDryWeight(CalculateDryReq calculateDryReq) {
        if (calculateDryReq == null || calculateDryReq.getWetWeight() == null
                || calculateDryReq.getWetWeight().compareTo(new BigDecimal(0.0)) != 1) {
            return CalculateDryWeiResp.builder().optResult(CalculateDryWeiResp.STATUS_ENUE.PARAM_ERROR.getValue())
                    .build();
        }

        double dryWetRadio = ((30.0 / 100) - (14.5 / 100)) * 1.2;
        BigDecimal dryWeight = calculateDryReq.getWetWeight()
                .subtract(calculateDryReq.getWetWeight().multiply(new BigDecimal(dryWetRadio)));

        return CalculateDryWeiResp.builder().optResult(CalculateDryWeiResp.STATUS_ENUE.SUCCESS.getValue()).
                dryWeight(dryWeight.setScale(2, ROUND_HALF_UP)).wetWeight(calculateDryReq.getWetWeight()).build();

    }

    private boolean checkQuote(SystemQuoteV2Req offerQuoteReq) {
        if (offerQuoteReq == null || offerQuoteReq.getCommodityId() == null || Strings
                .isNullOrEmpty(offerQuoteReq.getTowerId()) || offerQuoteReq.getShipmentWeight() == null
                || offerQuoteReq.getQuote() == null) {
            return false;
        }
        if (offerQuoteReq.getCargoStatus().equals(QuoteInfo.CARGO_STATUS.futures.getValue())
                && offerQuoteReq.getWetWeight() == null) {
            return false;
        }
        return true;

    }

    public boolean updateLoanInfo(QuoteInfo oldQuoteInfo, BigDecimal updatePrice, QuoteInfo newQuote) {
        if (updatePrice == null) {
            newQuote.setLoanId(oldQuoteInfo.getLoanId());
            return true;
        }
        if (oldQuoteInfo.getLoanId() == null) {
            LoanInfo loanInfo = initLoan(CookieAuthUtils.getCurrentUser(), updatePrice);
            int res = loanInfoMapper.insertSelective(loanInfo);
            if (res != 1) {
                log.error("update loan err due to insert loan db err");
                return false;
            }
            newQuote.setLoanId(Sets.newHashSet(loanInfo.getLoanId()));
            return true;
        }
        LoanInfo oldLoanInfo = loanInfoMapper.selectByPrimaryKey(oldQuoteInfo.getLoanId().iterator().next());
        if (oldLoanInfo == null) {
            LoanInfo loanInfo = initLoan(CookieAuthUtils.getCurrentUser(), updatePrice);
            int res = loanInfoMapper.insertSelective(loanInfo);
            if (res != 1) {
                log.error("update loan err due to insert loan db err");
                return false;
            }
            newQuote.setLoanId(Sets.newHashSet(loanInfo.getLoanId()));
            return true;
        }
        if (oldLoanInfo.getPrice().compareTo(updatePrice) == 0) {
            newQuote.setLoanId(oldQuoteInfo.getLoanId());
            return true;
        }
        try {
            int upRes = loanInfoMapper.updatePrice(updatePrice, oldQuoteInfo.getLoanId().iterator().next());
            if (upRes != 1) {
                log.error("update loan err due to update loan db err");
                return false;
            }
        } catch (Exception e) {
            log.error("update laodn into db error", e);
            return false;
        }
        newQuote.setLoanId(oldQuoteInfo.getLoanId());
        return true;
    }

    LoanInfo initLoan(String userId, BigDecimal price) {
        LoanInfo loanInfo = new LoanInfo();
        loanInfo.setLoanId(UUID.randomUUID().toString());
        loanInfo.setApplyTime(new Timestamp(System.currentTimeMillis()));
        loanInfo.setApplyUserId(userId);
        loanInfo.setPrice(price);
        return loanInfo;
    }

    public ConfirmOrderResp confirmOrder(ConfirmOrderReq confirmOrderReq) {
        if (confirmOrderReq == null || Strings.isNullOrEmpty(confirmOrderReq.getShowWarehouseTime())
                || confirmOrderReq.getQuote() == null || Strings.isNullOrEmpty(confirmOrderReq.getQuoteId())
                || confirmOrderReq.getQuote() == null) {
            return ConfirmOrderResp.builder().optResult(ConfirmOrderResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        QuoteInfo quoteInfo = quoteInfoDao.selectByPrimaryKey(confirmOrderReq.getQuoteId());
        if (quoteInfo == null) {
            return ConfirmOrderResp.builder().optResult(ConfirmOrderResp.STATUS_ENUE.ORDER_ERROR.getValue()).build();
        }
        if (!quoteInfo.getStatus().equals(QuoteInfo.QUOTE_TATUS.SER_REVIEW_PASS.getValue())) {
            return ConfirmOrderResp.builder().optResult(ConfirmOrderResp.STATUS_ENUE.ORDER_UNCHANGE.getValue()).build();
        }

        if (!quoteInfo.getUserId().equals(CookieAuthUtils.getCurrentUser())) {
            return ConfirmOrderResp.builder().optResult(ConfirmOrderResp.STATUS_ENUE.USER_ERROR.getValue()).build();
        }
        int res = quoteInfoDao.userConfirmOrderInfo(confirmOrderReq.getQuoteId(),
                TimeStampUtil.stringConvertTimeStamp(DATE_TIME_FORMAT, confirmOrderReq.getShowWarehouseTime()),
                confirmOrderReq.getQuote(), confirmOrderReq.getShipmentWeight(),
                QuoteInfo.QUOTE_TATUS.SER_REVIEW_PASS.getValue(), QuoteInfo.QUOTE_TATUS.UNDER_SER_REVIEW.getValue());

        if (res != 1) {
            log.error("user confirm order info fail db info is {},user update info is {}",
                    JacksonUtils.obj2String(quoteInfo), JacksonUtils.obj2String(confirmOrderReq));
            return ConfirmOrderResp.builder().optResult(ConfirmOrderResp.STATUS_ENUE.SERVER_ERR.getValue()).build();
        }
        return ConfirmOrderResp.builder().optResult(ConfirmOrderResp.STATUS_ENUE.SUCCESS.getValue())
                .status(QuoteInfo.QUOTE_TATUS.UNDER_SER_REVIEW.getValue()).build();
    }

}
