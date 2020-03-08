package com.demai.cornel.demeManager.service;

import com.demai.cornel.dao.CommodityDao;
import com.demai.cornel.dao.QuoteInfoDao;
import com.demai.cornel.dao.SystemQuoteDao;
import com.demai.cornel.demeManager.dao.AdminUserMapper;
import com.demai.cornel.demeManager.model.AdminUser;
import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.model.QuoteInfo;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.quota.GerQuoteListResp;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    13:34
 */
@Service
@Slf4j
public class AdminCornService {
    @Resource private SystemQuoteDao systemQuoteDao;
    @Resource private CommodityDao commodityDao;
    @Resource private QuoteInfoDao quoteInfoDao;

    @Resource private AdminUserMapper adminUserMapper;


    public List<AdminGetQuoteListResp> getQuoteList(GetQuoteListReq getQuoteListReq){
        List<AdminGetQuoteListResp> gerQuoteListResps =  quoteInfoDao.adminGetQuoteList(getQuoteListReq.getQuoteId(),
                Optional.ofNullable(getQuoteListReq.getPgSize()).orElse(10));
        if(gerQuoteListResps==null){
            log.warn("admin get system quote empty");
            return Collections.EMPTY_LIST;
        }
        return gerQuoteListResps;
    }


    public AdminGetQuteDetail getQuteDetail(String quoteId) {
        AdminGetQuteDetail quoteInfo = quoteInfoDao.adminGetQuoteDetail(quoteId);

        AdminUser adminUser = adminUserMapper.selectUserByUserId(CookieAuthUtils.getCurrentUser());

        if (adminUser == null) {
            log.warn("admin get  quote detail fail due to user error ");
            return AdminGetQuteDetail.builder().optStatus(AdminGetQuteDetail.STATUS_ENUE.USER_ERROR.getValue()).build();
        }

        if (quoteInfo == null) {
            log.warn("admin get  quote detail ");
            return AdminGetQuteDetail.builder().optStatus(AdminGetQuteDetail.STATUS_ENUE.ORDER_INVALID.getValue()).build();
        }
        quoteInfo.setOptStatus(AdminGetQuteDetail.STATUS_ENUE.SUCCESS.getValue());
        return quoteInfo;
    }
    public ReviewQuoteResp adminReviewQuote(ReviewQuoteReq quoteReq){
        if(quoteReq==null || Strings.isNullOrEmpty(quoteReq.getQuoteId()) || quoteReq.getStatus()==null){
            log.debug("review quote fail due to param error");
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }

        if(!quoteReq.getStatus().equals(QuoteInfo.QUOTE_TATUS.REVIEW_PASS.getValue())
                && !quoteReq.getStatus().equals(QuoteInfo.QUOTE_TATUS.REVIEW_REFUSE.getValue() )){
            log.debug("review quote fail due to param error quote status invalid ");
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        AdminUser adminUser = adminUserMapper.selectUserByUserId(CookieAuthUtils.getCurrentUser());
        if (adminUser == null) {
            log.warn("review quote failfail due to user error ");
            return ReviewQuoteResp.builder().optStatus(AdminGetQuteDetail.STATUS_ENUE.USER_ERROR.getValue()).build();
        }
        QuoteInfo quoteInfo = new QuoteInfo();
        quoteInfo.setQuoteId(quoteReq.getQuoteId());
        quoteInfo.setStatus(quoteReq.getStatus());
        quoteInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        quoteInfo.setReviewUser(CookieAuthUtils.getCurrentUser());
        int res = quoteInfoDao.updateByPrimaryKeySelective(quoteInfo);
        if(res!=1){
            log.warn("review quote failfail due to update db fail");
            return ReviewQuoteResp.builder().optStatus(AdminGetQuteDetail.STATUS_ENUE.SERVER_ERROR.getValue()).build();
        }
        return ReviewQuoteResp.builder().optStatus(AdminGetQuteDetail.STATUS_ENUE.SUCCESS.getValue()).build();

    }





}
