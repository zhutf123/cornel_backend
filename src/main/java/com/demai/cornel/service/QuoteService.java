package com.demai.cornel.service;

import com.demai.cornel.constant.ContextConsts;
import com.demai.cornel.dao.*;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.model.*;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.util.TimeStampUtil;
import com.demai.cornel.vo.quota.*;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author binz.zhang
 * @Date: 2019-12-31    13:50
 */
@Service @Slf4j public class QuoteService {

    @Resource private QuoteInfoDao quoteInfoDao;
    @Resource private UserInfoDao userInfoDao;
    @Resource private DryTowerDao dryTowerDao;
    @Resource private SystemQuoteDao systemQuoteDao;
    @Resource private CommodityDao commodityDao;
    private static String TIME_FORMAT = "yyyy-MM-dd";

    /**
     * 我要报价 不是从list 过来的 而是是我要报价的按钮
     *
     * @param offerQuoteReq
     * @return
     */
    public OfferQuoteResp offerQuote(OfferQuoteReq offerQuoteReq) {
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
        quoteInfo.setMobile(offerQuoteReq.getMobile());
        quoteInfo.setSystemFlag(QuoteInfo.SYSTEM_STATUS.USER_DEFINE.getValue());
        quoteInfo.setUserName(userInfoDao.getUserNameByUserId(userId));
        quoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.REVIEW.getValue());
        quoteInfo.setQuoteId(UUID.randomUUID().toString());
        quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getStartTime()));
        quoteInfo.setEndTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getEndTime()));
        quoteInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        quoteInfoDao.insertSelective(quoteInfo);
        offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.SUCCESS.getValue());
        offerQuoteResp.setQuoteStatus(quoteInfo.getStatus());
        offerQuoteResp.setQuoteId(quoteInfo.getQuoteId());
        return offerQuoteResp;
    }

    /**
     * 我要报价 从list 点击进来的
     *
     * @param offerQuoteReq
     * @return
     */
    public OfferQuoteResp offerSystemQuote(OfferQuoteReq offerQuoteReq) {
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
        quoteInfo.setMobile(offerQuoteReq.getMobile());
        quoteInfo.setSystemFlag(QuoteInfo.SYSTEM_STATUS.SYSTEM.getValue());
        quoteInfo.setUserName(userInfoDao.getUserNameByUserId(userId));
        quoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.REVIEW.getValue());
        quoteInfo.setQuoteId(UUID.randomUUID().toString());
        quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getStartTime()));
        quoteInfo.setEndTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getEndTime()));
        quoteInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        quoteInfoDao.insertSelective(quoteInfo);
        offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.SUCCESS.getValue());
        offerQuoteResp.setQuoteStatus(quoteInfo.getStatus());
        offerQuoteResp.setQuoteId(quoteInfo.getQuoteId());
        return offerQuoteResp;
    }

    /**
     * 点击“我要报价” 返给客户端
     *
     * @return
     */
    public OfferQuoteReq clickQuoteRest() {
        String userId = CookieAuthUtils.getCurrentUser();
        String location = dryTowerDao.getLocationByUserId(userId);
        return OfferQuoteReq.builder().location(location).shipmentWeight(ContextConsts.MIN_SHIPMENT_WEIGHT).build();

    }

    /**
     * 获取系统商品报价
     *
     * @param getQuoteListReq
     * @return
     */
    public List<GerQuoteListResp> getSystemQuoteList(GetQuoteListReq getQuoteListReq) {
        return systemQuoteDao.getNewSystemQuote(getQuoteListReq.getQuoteId(),
                Optional.ofNullable(getQuoteListReq.getPgSize()).orElse(10));
    }

    private boolean checkQuote(OfferQuoteReq offerQuoteReq) {
        return offerQuoteReq != null && offerQuoteReq.getCommodityId() != null && !Strings
                .isNullOrEmpty(offerQuoteReq.getTowerId()) && offerQuoteReq.getShipmentWeight() != null
                && offerQuoteReq.getQuote() != null;
    }

    /**
     * 获取议价的范围
     *
     * @param commodityId
     * @return
     */
    public BargainRange getBargainRange(String commodityId) {
        return BargainRange.builder().upper("+5").down("-5").build();
    }

    /**
     * 从系统报价列表点击进去我要报价返给客户端补全信息
     *
     * @param userId
     * @param commodityId
     * @return
     */
    public ClickSystemQuoteResp getClickInfo(String userId, String commodityId) {
        Preconditions.checkNotNull(userId);
        Preconditions.checkNotNull(commodityId);
        UserInfo userInfo = userInfoDao.getUserInfoByUserId(userId);
        if (userInfo == null || !userInfo.getRole().equals(UserInfo.ROLE_ENUE.SUPPLIER.getValue())) {
            return ClickSystemQuoteResp.builder().status(ClickSystemQuoteResp.STATUS_ENUE.USER_ERROR.getValue())
                    .build();
        }
        List<DryTower> ownDryInfo = dryTowerDao.selectDryTowerByUserId(userId);
        if (CollectionUtils.isEmpty(ownDryInfo)) {
            return ClickSystemQuoteResp.builder().status(ClickSystemQuoteResp.STATUS_ENUE.DRY_TOWER_ERROR.getValue())
                    .build();
        }
        Commodity commodity = commodityDao.getCommodityByCommodityId(commodityId);
        if (commodity == null) {
            return ClickSystemQuoteResp.builder().status(ClickSystemQuoteResp.STATUS_ENUE.COMMODITY_ERROR.getValue())
                    .build();
        }
        ClickSystemQuoteResp clickSystemQuoteResp = new ClickSystemQuoteResp();
        clickSystemQuoteResp.setUserName(userInfo.getName());
        if (userInfo.getMobile() != null && userInfo.getMobile().size() >= 1) {
            clickSystemQuoteResp.setMobile(userInfo.getMobile().iterator().next());
        }
        SystemQuote systemQuote = systemQuoteDao.getSystemQuoteByCommodityId(commodityId);
        clickSystemQuoteResp.setCommodity(commodity);
        clickSystemQuoteResp.setShipmentWeight(ContextConsts.MIN_SHIPMENT_WEIGHT);
        clickSystemQuoteResp.setUnitWeight(systemQuote.getUnitWeight());
        clickSystemQuoteResp.setUnitPrice(systemQuote.getUnitPrice());
        clickSystemQuoteResp.setQuote(systemQuote.getQuote());
        clickSystemQuoteResp.setStatus(ClickSystemQuoteResp.STATUS_ENUE.SUCCESS.getValue());
        List<ClickSystemQuoteResp.DryTowerInfo> dryTowerInfo = new ArrayList<>(ownDryInfo.size());
        ownDryInfo.stream().forEach(x -> {
            dryTowerInfo.add(new ClickSystemQuoteResp.DryTowerInfo(String.valueOf(x.getTowerId()), x.getLocation(),
                    x.getLocationArea(), x.getLocationDetail()));
        });
        clickSystemQuoteResp.setDryTowerInfo(dryTowerInfo);
        return clickSystemQuoteResp;
    }

    public List<GetOfferListResp> getOfferListRespList(String quoteId, Integer pgSize) {
        if (pgSize == null) {
            pgSize = 10;
        }
        return quoteInfoDao.getOwnerQuoteList(CookieAuthUtils.getCurrentUser(), quoteId, pgSize);
    }

    public List<GetOfferListResp> getSystemOfferListRespList(String quoteId, Integer pgSize) {
        if (pgSize == null) {
            pgSize = 10;
        }
        return quoteInfoDao.getSystemOwnerQuoteList(CookieAuthUtils.getCurrentUser(), quoteId, pgSize);
    }

    public GetOfferInfoResp getOfferInfoResp(String quoteId) {
        return quoteInfoDao.getQuoteInfoById(quoteId);
    }

    public OfferQuoteResp cancleQuote(String quoteId) {
        int result = quoteInfoDao.updateStatusByQuoteIdAndUserId(quoteId, CookieAuthUtils.getCurrentUser(),
                QuoteInfo.QUOTE_TATUS.CANCEL.getValue());
        OfferQuoteResp offerQuoteResp = new OfferQuoteResp();
        offerQuoteResp.setQuoteId(quoteId);
        if (result == 0) {
            offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.SERVER_ERROR.getValue());
            return offerQuoteResp;
        }
        offerQuoteResp.setQuoteStatus(QuoteInfo.QUOTE_TATUS.CANCEL.getValue());
        return offerQuoteResp;
    }

    /**
     * 根据起始时间获取时间的范围
     *
     * @param start
     * @param end
     * @return
     * @throws ParseException
     */
    private List<Date> getIntervalDate(String start, String end) {
        List<Date> dateList = new ArrayList<>();
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(start);//定义起始日期
            Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(end);//定义结束日期
            if (d1.after(d2)) {
                log.info("get the interval date fail due to the start after end");
                throw new IllegalArgumentException("get the interval date fail due to the start after end");
            }
            Calendar dd = Calendar.getInstance();//定义日期实例
            dd.setTime(d1);//设置日期起始时间
            while (dd.getTime().before(d2)) {//判断是否到结束日期
                dateList.add(dd.getTime());
                dd.add(Calendar.DATE, 1);//进行当前日期加1
            }
            dateList.add(d2);
        } catch (Exception e) {
            log.error("time parse error ", e);
            return null;
        }
        return dateList;

    }
}
