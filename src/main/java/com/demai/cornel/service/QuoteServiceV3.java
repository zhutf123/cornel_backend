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
import com.demai.cornel.vo.quota.OfferQuoteReq;
import com.demai.cornel.vo.quota.OfferQuoteResp;
import com.demai.cornel.vo.quota.SystemQuoteV2Req;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

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

    private static String TIME_FORMAT = "yyyy-MM-dd";

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

        QuoteInfo quoteInfo = new QuoteInfo();
        BeanUtils.copyProperties(offerQuoteReq, quoteInfo);
        String userId = CookieAuthUtils.getCurrentUser();
        quoteInfo.setLocation(dryTower.getLocation());
        quoteInfo.setUserId(userId);
        if (offerQuoteReq.getLoanPrice() != null) {
            LoanInfo loanInfo = new LoanInfo();
            loanInfo.setApplyUserId(CookieAuthUtils.getCurrentUser());
            loanInfo.setApplyTime(new Timestamp(System.currentTimeMillis()));
            loanInfo.setLoanId(UUID.randomUUID().toString());
            loanInfo.setPrice(offerQuoteReq.getLoanPrice());
            int res = loanInfoMapper.insertSelective(loanInfo);
            if (res != 1) {
                log.error("insert loan info into db error ");
                offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.SERVER_ERROR.getValue());
                return offerQuoteResp;
            }
            quoteInfo.setLoanId(Sets.newHashSet(loanInfo.getLoanId()));
        }
        if (Strings.isNullOrEmpty(offerQuoteReq.getMobile())) {
            UserInfo userInfo = userInfoDao.getUserInfoByUserId(CookieAuthUtils.getCurrentUser());
            if (!CollectionUtils.isEmpty(userInfo.getMobile())) {
                quoteInfo.setMobile(userInfo.getMobile().iterator().next());
            }
        }
        Timestamp warehouseTime = new Timestamp(
                TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getWarehouseTime()).getTime()
                        + System.currentTimeMillis() % 100000);//为了排序加上当前时间时分秒作为时间戳
        quoteInfo.setSystemFlag(QuoteInfo.SYSTEM_STATUS.SYSTEM.getValue());
        quoteInfo.setUserName(userInfoDao.getUserNameByUserId(userId));
        quoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.REVIEW.getValue());
        quoteInfo.setQuoteId(UUID.randomUUID().toString());
        quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getStartTime()));
        quoteInfo.setEndTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getEndTime()));
        quoteInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        quoteInfo.setWarehouseTime(warehouseTime);
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
        if (oldQuote.getStatus().equals(QuoteInfo.QUOTE_TATUS.REVIEW_PASS.getValue())) {
            log.info("edit  quote info fail due to  quote STATUS can't update ");
            offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.ORDER_STATUS_INVALD.getValue());
            return offerQuoteResp;
        }
        if (oldQuote.getUserId().equals(CookieAuthUtils.getCurrentUser())) {
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
        quoteInfo.setQuoteId(oldQuote.getQuoteId());
        quoteInfo.setSystemFlag(QuoteInfo.SYSTEM_STATUS.SYSTEM.getValue());
        quoteInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (!Strings.isNullOrEmpty(offerQuoteReq.getStartTime())) {
            quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getStartTime()));
        }
        if (!Strings.isNullOrEmpty(offerQuoteReq.getEndTime())) {
            quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getEndTime()));
        }
        if (!Strings.isNullOrEmpty(offerQuoteReq.getWarehouseTime())) {
            Timestamp warehouseTime = new Timestamp(
                    TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getWarehouseTime()).getTime()
                            + System.currentTimeMillis() % 100000);//为了排序加上当前时间时分秒作为时间戳
            quoteInfo.setWarehouseTime(warehouseTime);
        }
        quoteInfoDao.insertSelective(quoteInfo);
        offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.SUCCESS.getValue());
        offerQuoteResp.setQuoteStatus(quoteInfo.getStatus());
        offerQuoteResp.setQuoteId(quoteInfo.getQuoteId());
        return offerQuoteResp;
    }

    private boolean checkQuote(SystemQuoteV2Req offerQuoteReq) {
        return offerQuoteReq != null && offerQuoteReq.getCommodityId() != null && !Strings
                .isNullOrEmpty(offerQuoteReq.getTowerId()) && offerQuoteReq.getShipmentWeight() != null
                && offerQuoteReq.getQuote() != null && offerQuoteReq.getWetWeight() != null;
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
        int upRes = loanInfoMapper.updatePrice(updatePrice, oldQuoteInfo.getLoanId().iterator().next());
        if (upRes != 1) {
            log.error("update loan err due to update loan db err");
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
}
