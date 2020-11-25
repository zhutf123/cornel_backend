package com.demai.cornel.demeManager.service;

import com.demai.cornel.dao.*;
import com.demai.cornel.demeManager.dao.AdminUserMapper;
import com.demai.cornel.demeManager.dao.ReviewLogMapper;
import com.demai.cornel.demeManager.dao.SpecialQuoteMapper;
import com.demai.cornel.demeManager.vo.FinaReviewQuoteReq;
import com.demai.cornel.demeManager.vo.ReviewQuoteReq;
import com.demai.cornel.demeManager.vo.ReviewQuoteResp;
import com.demai.cornel.model.LoanInfo;
import com.demai.cornel.model.QuoteInfo;
import com.demai.cornel.service.ImgService;
import com.demai.cornel.service.QuoteService;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.TimeStampUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

/**
 * @Author binz.zhang
 * @Date: 2020-04-08    19:11
 */
@Slf4j @Service public class AdminReviewService {
    //    @Resource private SystemQuoteDao systemQuoteDao;
    //    @Resource private CommodityDao commodityDao;
    @Resource private QuoteInfoDao quoteInfoDao;

    //    @Resource private AdminUserMapper adminUserMapper;
    //    @Resource private DryTowerDao dryTowerDao;
    //    @Resource private UserInfoDao userInfoDao;
    //    @Resource private SpecialQuoteMapper specialQuoteMapper;
    //    @Resource private AdminUserLoginService adminUserLoginService;
    @Resource private LoanInfoMapper loanInfoMapper;
    @Resource private QuoteService quoteService;
    //    @Resource private ImgService imgService;

    //    @Resource private ReviewLogMapper reviewLogMapper;

    private static final String TIME_FORMT = "yyyy-MM-dd";

    /*业务审核烘干塔订单*/
    public ReviewQuoteResp bussiReviewQuote(ReviewQuoteReq quoteReq, String operator) {
        QuoteInfo oldQuote = quoteInfoDao.selectByPrimaryKey(quoteReq.getQuoteId());
        if (oldQuote == null) {
            log.error("bussiReviewQuote fail due to get quote from db err");
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.ORDER_INVALID.getValue()).build();
        }

        if (!oldQuote.getStatus().equals(QuoteInfo.QUOTE_TATUS.SER_REVIEW_PASS.getValue()) && !oldQuote.getStatus()
                .equals(QuoteInfo.QUOTE_TATUS.UNDER_SER_REVIEW.getValue())) {
            log.error("bussiReviewQuote fail due to  quote cur status is {} can not update {},{}", oldQuote.getStatus(),
                    QuoteInfo.QUOTE_TATUS.SER_REVIEW_PASS.getValue(),
                    QuoteInfo.QUOTE_TATUS.UNDER_SER_REVIEW.getValue());
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.ORDER_INVALID.getValue()).build();
        }
        QuoteInfo newQuoteInfo = new QuoteInfo();
        newQuoteInfo.setQuoteId(oldQuote.getQuoteId());
        switch (quoteReq.getOperaType()) {
        case (1):/*同意订单，直接流转到待财务审核，把之前的审核错误信息清除*/
            newQuoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.UNDER_FIN_REVIEW.getValue());
            HashMap<String, String> reviewOptU = new HashMap<>(2);
            reviewOptU.put("errCode", "0");
            reviewOptU.put("errDesc", "");
            newQuoteInfo.setReviewUser(operator);
            newQuoteInfo.setReviewUserTime(new Timestamp(System.currentTimeMillis()));
            newQuoteInfo.setReviewOpt(reviewOptU);
            break;
        case (2):/*拒绝，订单直接流转到拒绝。保留审核错误信息提示*/
            newQuoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.SER_REVIEW_REFUSE.getValue());
            if (quoteReq.getOperaType().equals(ReviewQuoteReq.OPERA_TYPE.REJECT.getValue())) {
                HashMap<String, String> reviewOpt = new HashMap<>(2);
                reviewOpt.put("errCode", String.valueOf(quoteReq.getErrCode()));
                reviewOpt.put("errDesc", quoteReq.getErrDesc());
                newQuoteInfo.setReviewUser(operator);
                newQuoteInfo.setReviewUserTime(new Timestamp(System.currentTimeMillis()));
                newQuoteInfo.setReviewOpt(reviewOpt);
            }
            break;
        case (3):/*修改了用户订单的部分信息，状态流转到待用户确认*/
            if (quoteReq.getQuote() != null) {
                newQuoteInfo.setQuote(quoteReq.getQuote());
            }
            if (quoteReq.getWarehouseTime() != null) {
                newQuoteInfo.setWarehouseTime(quoteService.getWarehouseTime(quoteReq.getWarehouseTime()));
            }
            if (quoteReq.getShipmentWeight() != null) {
                newQuoteInfo.setShipmentWeight(quoteReq.getShipmentWeight());
            }
            newQuoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.SER_REVIEW_PASS.getValue());
            break;
        default:
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        int resT = quoteInfoDao.updateByPrimaryKeySelective(newQuoteInfo);
        if (resT != 1) {
            log.error("bussiReviewQuote fail due to  quote  update DB fail");
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.SERVER_ERR.getValue()).build();
        }
        return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.SUCCESS.getValue()).build();
    }

    public ReviewQuoteResp finaReviewQuote(FinaReviewQuoteReq finaReviewQuoteReq, String operator) {
        QuoteInfo oldQuote = quoteInfoDao.selectByPrimaryKey(finaReviewQuoteReq.getQuoteId());
        if (oldQuote == null) {
            log.error("finaReviewQuote fail due to get quote from db err");
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.ORDER_INVALID.getValue()).build();
        }
        if (!oldQuote.getStatus().equals(QuoteInfo.QUOTE_TATUS.UNDER_FIN_REVIEW.getValue())) {
            log.error("bussiReviewQuote fail due to  quote cur status is {} can not update ", oldQuote.getStatus());
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.ORDER_INVALID.getValue()).build();
        }
        QuoteInfo newQuoteInfo = new QuoteInfo();
        newQuoteInfo.setQuoteId(oldQuote.getQuoteId());
        LoanInfo loanInfo = new LoanInfo();
        if (!(oldQuote.getLoanId() == null || oldQuote.getLoanId().size() == 0)) {
            log.error("finaReviewQuote fail due to  quote cur NO loan info ", oldQuote.getReviewStatus());
            loanInfo.setLoanId(oldQuote.getLoanId().iterator().next());
        }
        
        switch (finaReviewQuoteReq.getOperaType()) {
        case (1):/*同意，保存贷款信息，订单状态流转到财务审核通过*/
            newQuoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.FIN_REVIEW_PASS.getValue());
            loanInfo.setActualPrice(finaReviewQuoteReq.getActualPrice());
            loanInfo.setReviewUserId(CookieAuthUtils.getCurrentUser());
            loanInfo.setStatus(LoanInfo.STATUS.PROVER.getValue());
            loanInfo.setStartInterest(
                    TimeStampUtil.stringConvertTimeStamp(TIME_FORMT, finaReviewQuoteReq.getStartInterest()));
            loanInfo.setPrice(finaReviewQuoteReq.getActualPrice());

            newQuoteInfo.setFinanceUser(operator);
            newQuoteInfo.setFinanceUserTime(new Timestamp(System.currentTimeMillis()));

            break;
        case (2):/*拒绝状态流转到财务审核拒绝*/
            newQuoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.FIN_REVIEW_REJECT.getValue());
            loanInfo.setStatus(LoanInfo.STATUS.REJECT.getValue());
            HashMap<String, String> reviewOpt = new HashMap<>(2);

            reviewOpt.put("errDesc", finaReviewQuoteReq.getErrDesc());
            newQuoteInfo.setReviewOpt(reviewOpt);

            newQuoteInfo.setFinanceUser(operator);
            newQuoteInfo.setFinanceUserTime(new Timestamp(System.currentTimeMillis()));
            break;
        default:
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        int resT = quoteInfoDao.updateByPrimaryKeySelective(newQuoteInfo);
        if (resT != 1) {
            log.error("finaReviewQuote fail due to  quote  update DB fail");
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.SERVER_ERR.getValue()).build();
        }
        int resL = loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
        if (resL != 1) {
            log.error("finaReviewQuote fail due to  quote  update LOAN DB fail");
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.SERVER_ERR.getValue()).build();
        }
        return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.SUCCESS.getValue()).build();
    }

}
