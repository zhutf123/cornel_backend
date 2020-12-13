package com.demai.cornel.demeManager.service;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.demai.cornel.controller.quota.DryTowerController;
import com.demai.cornel.dao.*;
import com.demai.cornel.demeManager.dao.AdminUserMapper;
import com.demai.cornel.demeManager.dao.ReviewLogMapper;
import com.demai.cornel.demeManager.dao.SpecialQuoteMapper;
import com.demai.cornel.demeManager.model.AdminUser;
import com.demai.cornel.demeManager.model.ReviewLog;
import com.demai.cornel.demeManager.model.SpecialQuote;
import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.model.*;
import com.demai.cornel.purcharse.model.StoreInfo;
import com.demai.cornel.service.ImgService;
import com.demai.cornel.service.QuoteService;
import com.demai.cornel.service.SendMsgService;
import com.demai.cornel.service.UploadFileService;
import com.demai.cornel.util.Base64Utils;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.DateFormatUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.util.StringUtil;
import com.demai.cornel.vo.quota.GerQuoteListResp;
import com.demai.cornel.vo.quota.GetDryWetRadioResp;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hp.gagawa.java.elements.A;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    13:34
 */
@Service @Slf4j public class AdminCornService {
    @Resource private SystemQuoteDao systemQuoteDao;
    @Resource private CommodityDao commodityDao;
    @Resource private QuoteInfoDao quoteInfoDao;

    @Resource private DryTowerDao dryTowerDao;
    @Resource private UserInfoDao userInfoDao;
    @Resource private SpecialQuoteMapper specialQuoteMapper;
    @Resource private AdminUserLoginService adminUserLoginService;
    @Resource private LoanInfoMapper loanInfoMapper;
    @Resource private ImgService imgService;
    @Resource private AdminReviewService adminReviewService;
    @Resource private ReviewLogMapper reviewLogMapper;
    @Resource private StoreService storeService;
    @Resource private SendMsgService sendMsgService;

    /*财务人员获取指定烘干塔下的订单预览*/
    public List<AdminGetQuoteList> adminGetQuoteLists(Integer offset, Integer pgSize) {
        List<AdminGetQuoteList> quoteLists = dryTowerDao
                .selectDrytower(Optional.ofNullable(offset).orElse(0), Optional.ofNullable(pgSize).orElse(10));
        if (quoteLists == null) {
            return Collections.EMPTY_LIST;
        }
        quoteLists.stream().forEach(x -> {
            List<AdminGetQuoteList.orderInfo> orderInfos = quoteInfoDao.selectQuoteViewByTowerId(x.getTower_id());
            x.setTower_info(orderInfos == null ? Collections.EMPTY_LIST : orderInfos);
        });
        return quoteLists;
    }

    /*业务人员获取指定烘干塔下面的订单预览*/
    public List<AdminGetQuoteList> oPadminGetQuoteLists(Integer offset, Integer pgSize) {
        List<AdminGetQuoteList> quoteLists = dryTowerDao
                .selectDrytower(Optional.ofNullable(offset).orElse(0), Optional.ofNullable(pgSize).orElse(10));
        if (quoteLists == null) {
            return Collections.EMPTY_LIST;
        }
        quoteLists.stream().forEach(x -> {
            List<AdminGetQuoteList.orderInfo> orderInfos = quoteInfoDao.optselectQuoteViewByTowerId(x.getTower_id());
            x.setTower_info(orderInfos == null ? Collections.EMPTY_LIST : orderInfos);
        });
        return quoteLists;
    }

    public AdminGetQueFinResp adminGetQueFinInfo() throws ParseException {
        AdminGetQueFinResp adminGetQueFinResp = quoteInfoDao.adminGetFinInfo();
        if (adminGetQueFinResp == null) {
            return null;
        }
        adminGetQueFinResp.setAvg_price(
                adminGetQueFinResp.getPrice_count().divide(new BigDecimal(adminGetQueFinResp.getOrder_count()), 2));
        long days = 0;
        Date now = new Date(System.currentTimeMillis());
        Date startDate = DateFormatUtils.parse("2020-02-20");
        days = Math.abs(now.getTime() - startDate.getTime()) / (24 * 3600 * 1000);
        adminGetQueFinResp.setDays(days);
        adminGetQueFinResp.setAvg_interest(adminGetQueFinResp.getTotal_interest().divide(new BigDecimal(days), 2));
        return adminGetQueFinResp;
    }

    /*财务人员获取指定烘干塔下的订单list*/
    public List<AdminGetQuoteListResp> getQuoteList(GetQuoteListReq getQuoteListReq, HttpServletResponse response) {

        if (getQuoteListReq.getPgSize() == null) {
            getQuoteListReq.setPgSize(0);
        }
        List<AdminGetQuoteListResp> gerQuoteListResps = quoteInfoDao
                .adminGetQuoteList(Optional.ofNullable(getQuoteListReq.getOffset()).orElse(0),
                        Optional.ofNullable(getQuoteListReq.getPgSize()).orElse(10), getQuoteListReq.getTowerId());
        if (CollectionUtils.isEmpty(gerQuoteListResps)) {
            log.warn("admin get system quote empty");
            return Collections.EMPTY_LIST;
        }
        gerQuoteListResps.stream().forEach(res ->{
            res.setWarehouseTime(DateFormatUtils.format(DateFormatUtils.parseDateTime(res.getWarehouseTime())));
        });

        return gerQuoteListResps;
    }

    /*业务员获取指定烘干塔下的订单list*/
    public List<AdminGetQuoteListResp> OpgetQuoteList(GetQuoteListReq getQuoteListReq, HttpServletResponse response) {
        if (getQuoteListReq.getPgSize() == null) {
            getQuoteListReq.setPgSize(10);
        }
        List<AdminGetQuoteListResp> gerQuoteListResps = quoteInfoDao
                .opPdminGetQuoteList(Optional.ofNullable(getQuoteListReq.getOffset()).orElse(0),
                        Optional.ofNullable(getQuoteListReq.getPgSize()).orElse(10), getQuoteListReq.getTowerId());
        if (CollectionUtils.isEmpty(gerQuoteListResps)) {
            log.warn("admin get system quote empty");
            return Collections.EMPTY_LIST;
        }
        gerQuoteListResps.stream().forEach(res ->{
            res.setWarehouseTime(DateFormatUtils.format(DateFormatUtils.parseDateTime(res.getWarehouseTime())));
            if(StringUtil.isNotEmpty(res.getReviewUserId())){
                UserInfo info = userInfoDao.getUserInfoByUserId(res.getReviewUserId());
                if (info!=null){
                    res.setReviewUser(info.getName());
                }
            }

            if(StringUtil.isNotEmpty(res.getFinanceUserId())){
                UserInfo info = userInfoDao.getUserInfoByUserId(res.getFinanceUserId());
                if (info!=null){
                    res.setFinanceUser(info.getName());
                }
            }
        });
        return gerQuoteListResps;
    }

    public AdminGetQuteDetail getQuteDetail(String quoteId) {

        String userId = CookieAuthUtils.getCurrentUser();
        String token = CookieAuthUtils.getCurrentUserToken();
        if (!adminUserLoginService.checkAdminToken(token, userId)) {
            return AdminGetQuteDetail.builder().optStatus(AdminGetQuteDetail.STATUS_ENUE.USER_ERROR.getValue()).build();
        }
        AdminGetQuteDetail quoteInfo = quoteInfoDao.adminGetQuoteDetail(quoteId);
        if (quoteInfo == null) {
            log.warn("admin get  quote detail ");
            return AdminGetQuteDetail.builder().optStatus(AdminGetQuteDetail.STATUS_ENUE.ORDER_INVALID.getValue())
                    .build();
        }
        if (quoteInfo.getLoanId() == null) {
            quoteInfo.setLoanPrice(new BigDecimal(0.0));
        } else {
            List<LoanInfo> loanInfos = loanInfoMapper.getLoanByLoanIds(quoteInfo.getLoanId());
            quoteInfo.setLoanPrice(
                    loanInfos == null || loanInfos.size() == 0 ? new BigDecimal(0) : loanInfos.get(0).getPrice());
        }
        quoteInfo.setImgInfo(imgService.getQuoteImgs(quoteId));
        quoteInfo.setDryWetRadio(GetDryWetRadioResp.dryWetRadio);
        quoteInfo.setOptStatus(AdminGetQuteDetail.STATUS_ENUE.SUCCESS.getValue());
        quoteInfo.setWarehouseTime(DateFormatUtils.format(DateFormatUtils.parseDateTime(quoteInfo.getWarehouseTime())));
        return quoteInfo;
    }

    /*业务人员审核烘干塔订单*/
    public ReviewQuoteResp adminReviewQuote(ReviewQuoteReq quoteReq) {

        if (quoteReq == null || Strings.isNullOrEmpty(quoteReq.getQuoteId())) {
            log.debug("review quote fail due to param error");
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }

        if (quoteReq.getOperaType().equals(ReviewQuoteReq.OPERA_TYPE.REJECT.getValue()) && (
                quoteReq.getErrCode() == null || Strings.isNullOrEmpty(quoteReq.getErrDesc()))) {
            log.debug("review quote fail due to param error reject quote must give the reason");
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }

        ReviewQuoteResp quoteResp = adminReviewService.bussiReviewQuote(quoteReq, CookieAuthUtils.getCurrentUser());
        if (!storeService.convertStore(quoteReq.getQuoteId())) {
            log.debug("quote convert store fail");
        } else {
            log.debug("quote convert store sucess");
        }
        return quoteResp;
    }

    /*业务人员审核订单*/
    public ReviewQuoteResp finceReviewQuote(FinaReviewQuoteReq finaReviewQuoteReq) {
        if (finaReviewQuoteReq == null || Strings.isNullOrEmpty(finaReviewQuoteReq.getQuoteId())
                || finaReviewQuoteReq.getOperaType() == null) {
            log.debug("review quote fail due to param error");
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }

        if (finaReviewQuoteReq.getOperaType().equals(FinaReviewQuoteReq.OPERA_TYPE.APPROVEL.getValue()) && (
                finaReviewQuoteReq.getActualPrice() == null || Strings
                        .isNullOrEmpty(finaReviewQuoteReq.getStartInterest()))) {
            log.debug("review quote fail due to param error quote status invalid ");
            return ReviewQuoteResp.builder().optStatus(ReviewQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        ReviewLog reviewLog = new ReviewLog();
        reviewLog.setOperatorTime(new Timestamp(System.currentTimeMillis()));
        reviewLog.setOperatorUser(CookieAuthUtils.getCurrentUser());
        reviewLog.setOperatorType(ReviewLog.OPERATOR_TYPE_ENUM.Financial.getValue());
        reviewLog.setOptType(finaReviewQuoteReq.getOperaType());
        reviewLog.setOrderId(finaReviewQuoteReq.getQuoteId());
        //reviewLog.setReviewOpt(quoteReq.ge);
        ReviewQuoteResp quoteResp = adminReviewService.finaReviewQuote(finaReviewQuoteReq, CookieAuthUtils.getCurrentUser());
        if (quoteResp.getOptStatus().equals(ReviewQuoteResp.STATUS_ENUE.SUCCESS.getValue())) {
            reviewLogMapper.insertSelective(reviewLog);
        }
        return quoteResp;
    }

    public List<AdminGetTowerListResp> getTowerList(AdminGetTowerReq adminGetTowerReq) {
        String userId = CookieAuthUtils.getCurrentUser();
        String token = CookieAuthUtils.getCurrentUserToken();
        if (!adminUserLoginService.checkAdminToken(token, userId)) {
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
        String userId = CookieAuthUtils.getCurrentUser();
        String token = CookieAuthUtils.getCurrentUserToken();
        if (!adminUserLoginService.checkAdminToken(token, userId)) {
            return AdminEditQuoteResp.builder().optStatus(AdminEditQuoteResp.STATUS_ENUE.USER_ERROR.getValue()).build();
        }
        if (adminGetTowerReq == null) {
            log.debug("cur user {} no auth EDIT quote due to param error", CookieAuthUtils.getCurrentUser());
            return AdminEditQuoteResp.builder().optStatus(AdminEditQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue())
                    .build();
        }
        for (AdminEditQuoteReq.quoteInfo x : adminGetTowerReq.getQuoteInfos()) {
            if (x.getSystemFlag() != null && x.getSystemFlag() == AdminEditQuoteReq.QUOTE_FLAG.SYSTEM_FLAG.getValue()) {
                editSystemQuote(x);
            } else {
                editSpecialUserQuote(x);
            }
        }
        return AdminEditQuoteResp.builder().optStatus(AdminEditQuoteResp.STATUS_ENUE.SUCCESS.getValue()).build();
    }

    private boolean editSpecialUserQuote(AdminEditQuoteReq.quoteInfo quoteInfo) {
        if (quoteInfo == null || Strings.isNullOrEmpty(quoteInfo.getCommodityId()) || Strings
                .isNullOrEmpty(quoteInfo.getUserId()) || Strings.isNullOrEmpty(quoteInfo.getTowerId())
                || quoteInfo.getQuote() == null) {
            log.error("editSpecialUserQuote fail due param lock param is {}", JacksonUtils.obj2String(quoteInfo));
            return false;
        }
        SpecialQuote specialQuote = new SpecialQuote();
        SystemQuote systemQuote = systemQuoteDao.selectByCommodityId(quoteInfo.getCommodityId());
        if (systemQuote != null) {
            BeanUtils.copyProperties(systemQuote, specialQuote);
        }
        BeanUtils.copyProperties(quoteInfo, specialQuote);
        specialQuote.setQuoteUserId(CookieAuthUtils.getCurrentUser());
        specialQuote.setTargetUserId(quoteInfo.getUserId());
        specialQuote.setTargetTowerId(quoteInfo.getTowerId());

        specialQuote.setQuote(quoteInfo.getSelfQuote());
        Commodity commodity = commodityDao.getCommodityByCommodityId(specialQuote.getCommodityId());
        DryTower tower = dryTowerDao.selectByQuoteId(quoteInfo.getTowerId());
        Set<String> userIds = tower.getContactUserId();
        userIds.add(tower.getBindUserId());
        List<UserInfo> userInfo = userInfoDao.getUserInfoByUserIds(userIds);
        Set<String> mobiles = Sets.newHashSet();
        userInfo.forEach(user -> {mobiles.addAll(user.getMobile());});
        sendMsgService.sendPriceChangeMsg(mobiles, commodity.getName(), specialQuote.getQuote());

        specialQuoteMapper.updateCommodityIdquoteStatus(quoteInfo.getCommodityId(), quoteInfo.getUserId());
        int res = specialQuoteMapper.insertSelective(specialQuote);
        if (res != 1) {
            log.error("editSpecialUserQuote fail due to update db fail");
            return false;
        }
        return true;
    }

    private boolean editSystemQuote(AdminEditQuoteReq.quoteInfo quoteInfo) {
        if (quoteInfo == null || Strings.isNullOrEmpty(quoteInfo.getCommodityId()) || quoteInfo.getQuote() == null) {
            log.error("editSystemQuote fail due param lock param is {}", JacksonUtils.obj2String(quoteInfo));
            return false;
        }
        SystemQuote systemQuoteOld = systemQuoteDao.getSystemQuoteByCommodityId(quoteInfo.getCommodityId());
        if (systemQuoteOld == null) {
            log.error("editSystemQuote fail due to systemQuoteOld is null");
        }
        SystemQuote systemQuoteNew = new SystemQuote();
        BeanUtils.copyProperties(systemQuoteOld, systemQuoteNew);
        systemQuoteNew.setStatus(1);
        systemQuoteNew.setId(null);
        systemQuoteNew.setQuote(quoteInfo.getSelfQuote());
        systemQuoteNew.setQuoteUserId(CookieAuthUtils.getCurrentUser());
        systemQuoteNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
        systemQuoteNew.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        systemQuoteDao.updateCommoditySystemInvalid(quoteInfo.getCommodityId());
        int res = systemQuoteDao.insertSelective(systemQuoteNew);
        if (res != 1) {
            log.error("editSystemQuote fail due to update db fail");
            return false;
        }
        return true;
    }

    public List<AdminGetSyQuLis> getSyQuLis(String towerUserId) {
        String userId = CookieAuthUtils.getCurrentUser();
        String token = CookieAuthUtils.getCurrentUserToken();
        if (!adminUserLoginService.checkAdminToken(token, userId)) {
            return Collections.EMPTY_LIST;
        }   
        List<AdminGetSyQuLis> systemQuote = systemQuoteDao.adminGetSystemList();
        if (CollectionUtils.isEmpty(systemQuote)) {
            log.debug("cur user {}  get system quote list empty", CookieAuthUtils.getCurrentUser());
            return Collections.EMPTY_LIST;
        }

        if (StringUtils.isNotEmpty(towerUserId)){
            systemQuote.stream().forEach(x -> {
                SpecialQuote specialQuote = specialQuoteMapper
                        .selectSpecialQuoteByCommodityId(towerUserId, x.getCommodityId());
                x.setSysQuote(x.getQuote());
                if (specialQuote != null) {
                    x.setQuote(specialQuote.getQuote());
                    x.setUnitPrice(specialQuote.getUnitPrice());
                    x.setUnitWeight(specialQuote.getUnitWeight());
                }
            });
        }

        return systemQuote;
    }

    public List<AdminGetTowerQuLiResp> getTowerQuoteList(String towerUserId) {
        String userId = CookieAuthUtils.getCurrentUser();
        String token = CookieAuthUtils.getCurrentUserToken();
        if (!adminUserLoginService.checkAdminToken(token, userId)) {
            return Collections.EMPTY_LIST;
        }
        List<SpecialQuote> specialQuote = specialQuoteMapper.selectSpecialQuoteByTargetUserId(towerUserId);
        if (CollectionUtils.isEmpty(specialQuote)) {
            log.debug("cur user {}  get AdminGetTowerQuLiResp list empty", CookieAuthUtils.getCurrentUser());
            return Collections.EMPTY_LIST;
        }
        List<AdminGetTowerQuLiResp> resp = new ArrayList<>();
        specialQuote.stream().forEach(x -> {
            Commodity commodity = commodityDao.getCommodityByCommodityId(x.getCommodityId());
            AdminGetTowerQuLiResp adminGetTowerQuLiResp = new AdminGetTowerQuLiResp();
            BeanUtils.copyProperties(x, adminGetTowerQuLiResp);
            adminGetTowerQuLiResp.setCommodityName(commodity.getName());
            resp.add(adminGetTowerQuLiResp);
        });
        return resp;
    }

    public List<ReviewModel> getReviewErrOpt() {
        List<ReviewModel> reviewModels = new ArrayList<>();
        Arrays.stream(ReviewModel.TOWER_SUP_ORDER_ERR.values()).forEach(x -> {
            ReviewModel reviewModel = new ReviewModel();
            reviewModel.setErrCode(x.getValue());
            reviewModel.setDesc(x.getExpr());
            reviewModels.add(reviewModel);
        });
        return reviewModels;
    }

    public static void main(String[] args) {
        String ckey = "u=" + "binz.zhang" + "&t=";
        String encodeCkey = new String(Base64Utils.encode(ckey.getBytes()));
        String decode = new String(Base64Utils.decode(encodeCkey));
        System.out.println(decode);
    }

}
