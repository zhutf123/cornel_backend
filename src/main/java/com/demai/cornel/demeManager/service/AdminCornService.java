package com.demai.cornel.demeManager.service;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.demai.cornel.controller.quota.DryTowerController;
import com.demai.cornel.dao.*;
import com.demai.cornel.demeManager.dao.AdminUserMapper;
import com.demai.cornel.demeManager.dao.SpecialQuoteMapper;
import com.demai.cornel.demeManager.model.AdminUser;
import com.demai.cornel.demeManager.model.SpecialQuote;
import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.model.DryTower;
import com.demai.cornel.model.QuoteInfo;
import com.demai.cornel.model.SystemQuote;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.util.Base64Utils;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.vo.quota.GerQuoteListResp;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    13:34
 */
@Service @Slf4j public class AdminCornService {
    @Resource private SystemQuoteDao systemQuoteDao;
    @Resource private CommodityDao commodityDao;
    @Resource private QuoteInfoDao quoteInfoDao;

    @Resource private AdminUserMapper adminUserMapper;
    @Resource private DryTowerDao dryTowerDao;
    @Resource private UserInfoDao userInfoDao;
    @Resource private SpecialQuoteMapper specialQuoteMapper;

    public List<AdminGetQuoteListResp> getQuoteList(GetQuoteListReq getQuoteListReq) {
        List<AdminGetQuoteListResp> gerQuoteListResps = quoteInfoDao.adminGetQuoteList(getQuoteListReq.getQuoteId(),
                Optional.ofNullable(getQuoteListReq.getPgSize()).orElse(10));
        if (gerQuoteListResps == null) {
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
            return AdminGetQuteDetail.builder().optStatus(AdminGetQuteDetail.STATUS_ENUE.ORDER_INVALID.getValue())
                    .build();
        }
        quoteInfo.setOptStatus(AdminGetQuteDetail.STATUS_ENUE.SUCCESS.getValue());
        return quoteInfo;
    }

    public ReviewQuoteResp adminReviewQuote(ReviewQuoteReq quoteReq) {
        if (quoteReq == null || Strings.isNullOrEmpty(quoteReq.getQuoteId()) || quoteReq.getStatus() == null) {
            log.debug("review quote fail due to param error");
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }

        if (!quoteReq.getStatus().equals(QuoteInfo.QUOTE_TATUS.REVIEW_PASS.getValue()) && !quoteReq.getStatus()
                .equals(QuoteInfo.QUOTE_TATUS.REVIEW_REFUSE.getValue())) {
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
        if (res != 1) {
            log.warn("review quote failfail due to update db fail");
            return ReviewQuoteResp.builder().optStatus(AdminGetQuteDetail.STATUS_ENUE.SERVER_ERROR.getValue()).build();
        }
        return ReviewQuoteResp.builder().optStatus(AdminGetQuteDetail.STATUS_ENUE.SUCCESS.getValue()).build();

    }

    public List<AdminGetTowerListResp> getTowerList(AdminGetTowerReq adminGetTowerReq) {
        AdminUser adminUser = adminUserMapper.selectUserByUserId(CookieAuthUtils.getCurrentUser());
        if (adminUser == null) {
            log.debug("cur user {} no auth get tower list", CookieAuthUtils.getCurrentUser());
            return Collections.EMPTY_LIST;
        }
        List<DryTower> towers = dryTowerDao.selectAllTower(adminGetTowerReq.getTowerId(),
                Optional.ofNullable(adminGetTowerReq.getPgSize()).orElse(10));
        if (towers == null || towers.size() == 0) {
            log.debug("cur user {} no auth get tower list empty", CookieAuthUtils.getCurrentUser());
            return Collections.EMPTY_LIST;
        }
        List<AdminGetTowerListResp> getTowerListResps = new ArrayList<>();
        towers.stream().forEach(x -> {
            AdminGetTowerListResp adminGetTowerListResp = new AdminGetTowerListResp();
            BeanUtils.copyProperties(x, adminGetTowerListResp);
            UserInfo userInfo = userInfoDao.getUserInfoByUserId(x.getBindUserId());
            if (userInfo != null) {
                adminGetTowerListResp.setUserName(userInfo.getName());
                adminGetTowerListResp.setMobile(userInfo.getMobile().iterator().next());
            }
            getTowerListResps.add(adminGetTowerListResp);
        });
        return getTowerListResps;
    }

    public AdminEditQuoteResp editQuote(AdminEditQuoteReq adminGetTowerReq) {
        if (adminGetTowerReq == null ) {
            log.debug("cur user {} no auth EDIT quote due to param error", CookieAuthUtils.getCurrentUser());
            return AdminEditQuoteResp.builder().optStatus(AdminEditQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue())
                    .build();
        }
        AdminUser adminUser = adminUserMapper.selectUserByUserId(CookieAuthUtils.getCurrentUser());
        if (adminUser == null) {
            log.debug("cur user {} no auth EDIT quote", CookieAuthUtils.getCurrentUser());
            return AdminEditQuoteResp.builder().optStatus(AdminEditQuoteResp.STATUS_ENUE.USER_ERROR.getValue()).build();
        }


        for (AdminEditQuoteReq.quoteInfo x : adminGetTowerReq.getQuoteInfos()) {
            SpecialQuote specialQuote = new SpecialQuote();
            SystemQuote systemQuote = systemQuoteDao.selectByCommodityId(x.getCommodityId());
            if (systemQuote != null) {
                BeanUtils.copyProperties(systemQuote, specialQuote);
            }
            BeanUtils.copyProperties(x, specialQuote);
            specialQuote.setQuoteUserId(CookieAuthUtils.getCurrentUser());
            specialQuote.setTargetUserId(x.getUserId());
            specialQuote.setTargetUserId(x.getTowerId());
            specialQuote.setQuote(x.getSelfQuote());
            specialQuoteMapper.updateCommodityIdquoteStatus(x.getCommodityId(),x.getUserId());
            int res = specialQuoteMapper.insertSelective(specialQuote);
            if (res != 1) {
                return AdminEditQuoteResp.builder().optStatus(AdminEditQuoteResp.STATUS_ENUE.SUCCESS.getValue())
                        .build();

            }
        }
        return AdminEditQuoteResp.builder().optStatus(AdminEditQuoteResp.STATUS_ENUE.SUCCESS.getValue()).build();
    }

    public List<AdminGetSyQuLis> getSyQuLis() {
        List<AdminGetSyQuLis> systemQuote = systemQuoteDao.adminGetSystemList();
        if (systemQuote == null) {
            log.debug("cur user {}  get system quote list empty", CookieAuthUtils.getCurrentUser());
            return Collections.EMPTY_LIST;
        }
       return systemQuote;
    }

    public static void main(String[] args) {
        String ckey = "u="+"binz.zhang"+"&t=";
        String encodeCkey = new String(Base64Utils.encode(ckey.getBytes()));
        String decode = new String(Base64Utils.decode(encodeCkey));
        System.out.println(decode);
    }

}
