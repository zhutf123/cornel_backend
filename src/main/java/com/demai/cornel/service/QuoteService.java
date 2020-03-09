package com.demai.cornel.service;

import com.demai.cornel.config.ServiceMobileConfig;
import com.demai.cornel.constant.ContextConsts;
import com.demai.cornel.dao.*;
import com.demai.cornel.demeManager.dao.SpecialQuoteMapper;
import com.demai.cornel.demeManager.model.SpecialQuote;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.model.*;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.util.TimeStampUtil;
import com.demai.cornel.vo.quota.*;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.postgresql.jdbc.TimestampUtils;
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
import java.util.stream.Collectors;

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
    @Resource private CommodityService commodityService;
    @Resource private SpecialQuoteMapper specialQuoteMapper;

    private static String TIME_FORMAT = "yyyy-MM-dd";

    /**
     * 我要报价 不是从list 过来的 而是是我要报价的按钮
     *
     * @param offerQuoteReq
     * @return
     */
    public OfferQuoteResp offerQuote(UserDefineQuoteReq offerQuoteReq) {
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
        // todo 这一块后续需要重新做，用户上传的自定义的商品 为-1 的时候表示是用户自定义的
        if (offerQuoteReq.getCommodityId().equalsIgnoreCase("-1") && Strings
                .isNullOrEmpty(offerQuoteReq.getCommodityName())) {
            log.debug("offer quote fail due to commodity is empty");
            return OfferQuoteResp.builder().status(OfferQuoteResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        if (offerQuoteReq.getCommodityId().equalsIgnoreCase("-1")) {
            Commodity commodity = new Commodity();
            commodity.setName(offerQuoteReq.getCommodityName());
            offerQuoteReq.setCommodityId(commodityService.insertUserDefineCommodity(commodity));
        }
        QuoteInfo quoteInfo = new QuoteInfo();
        BeanUtils.copyProperties(offerQuoteReq, quoteInfo);
        String userId = CookieAuthUtils.getCurrentUser();
        quoteInfo.setLocation(dryTower.getLocation());
        if (Strings.isNullOrEmpty(offerQuoteReq.getMobile())) {
            UserInfo userInfo = userInfoDao.getUserInfoByUserId(CookieAuthUtils.getCurrentUser());
            if (!CollectionUtils.isEmpty(userInfo.getMobile())) {
                quoteInfo.setMobile(userInfo.getMobile().iterator().next());
            }
        }
        quoteInfo.setUserId(userId);
        quoteInfo.setSystemFlag(QuoteInfo.SYSTEM_STATUS.USER_DEFINE.getValue());
        quoteInfo.setUserName(userInfoDao.getUserNameByUserId(userId));
        quoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.REVIEW.getValue());
        quoteInfo.setQuoteId(UUID.randomUUID().toString());
        quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getStartTime()));
        quoteInfo.setEndTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getEndTime()));
        quoteInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        quoteInfo.setWarehouseTime(new Timestamp(System.currentTimeMillis()));
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
    public OfferQuoteResp offerSystemQuote(SystemQuoteReq offerQuoteReq) {
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
        if (Strings.isNullOrEmpty(offerQuoteReq.getMobile())) {
            UserInfo userInfo = userInfoDao.getUserInfoByUserId(CookieAuthUtils.getCurrentUser());
            if (!CollectionUtils.isEmpty(userInfo.getMobile())) {
                quoteInfo.setMobile(userInfo.getMobile().iterator().next());
            }
        }
        quoteInfo.setSystemFlag(QuoteInfo.SYSTEM_STATUS.SYSTEM.getValue());
        quoteInfo.setUserName(userInfoDao.getUserNameByUserId(userId));
        quoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.REVIEW.getValue());
        quoteInfo.setQuoteId(UUID.randomUUID().toString());
        quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getStartTime()));
        quoteInfo.setEndTime(TimeStampUtil.stringConvertTimeStamp(TIME_FORMAT, offerQuoteReq.getEndTime()));
        quoteInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        quoteInfo.setWarehouseTime(new Timestamp(System.currentTimeMillis()));

        quoteInfoDao.insertSelective(quoteInfo);
        offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.SUCCESS.getValue());
        offerQuoteResp.setQuoteStatus(quoteInfo.getStatus());
        offerQuoteResp.setQuoteId(quoteInfo.getQuoteId());
        return offerQuoteResp;
    }

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
        return OfferQuoteReq.builder().location(location).
                shipmentWeight(ContextConsts.MIN_SHIPMENT_WEIGHT).unitPrice(ContextConsts.DEFAULT_UNIT_PRICE)
                .unitWeight(ContextConsts.DEFAULT_UNIT_WEIGHT).build();

    }

    /**
     * 获取系统商品报价
     *
     * @param getQuoteListReq
     * @return
     */
    public List<GerQuoteListResp> getSystemQuoteList(GetQuoteListReq getQuoteListReq) {
        List<GerQuoteListResp> gerQuoteListResps = systemQuoteDao.getNewSystemQuote(getQuoteListReq.getQuoteId(),
                Optional.ofNullable(getQuoteListReq.getPgSize()).orElse(10));
        if (gerQuoteListResps == null) {
            log.warn("get system quote empty");
            return Collections.EMPTY_LIST;
        }
        List<SpecialQuote> specialQuote = specialQuoteMapper
                .selectSpecialQuoteByTargetUserId(CookieAuthUtils.getCurrentUser());
        Map<String, BigDecimal> bigDecimalHashMap = null;
        if (specialQuote != null) {
            bigDecimalHashMap = specialQuote.stream().collect(Collectors
                    .toMap(SpecialQuote::getCommodityId, SpecialQuote::getQuote, (oldValue, newValue) -> newValue));
        }
        if (bigDecimalHashMap != null) {
            Map<String, BigDecimal> finalBigDecimalHashMap = bigDecimalHashMap;
            gerQuoteListResps.stream().forEach(x -> {
                if (finalBigDecimalHashMap.get(x.getCommodityId()) != null) {
                    x.setQuote(finalBigDecimalHashMap.get(x.getCommodityId()));
                }
            });
        }
        buildSystemQuoteDetail(gerQuoteListResps);
        return gerQuoteListResps;

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
        List<ClickSystemQuoteResp.DryTowerInfo> dryTowerInfo = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ownDryInfo)) {
            ownDryInfo.stream().forEach(x -> {
                dryTowerInfo.add(new ClickSystemQuoteResp.DryTowerInfo(String.valueOf(x.getTowerId()), x.getLocation(),
                        x.getLocationArea(), x.getLocationDetail()));
            });
        }
        clickSystemQuoteResp.setDryTowerInfo(dryTowerInfo);
        return clickSystemQuoteResp;
    }

    public List<GetOfferListResp> getOfferListRespList(String quoteId, Integer pgSize) {
        if (pgSize == null) {
            pgSize = 10;
        }
        List<GetOfferListResp> getOfferListResps = quoteInfoDao
                .getOwnerQuoteList(CookieAuthUtils.getCurrentUser(), quoteId, pgSize);
        if (getOfferListResps == null) {
            getOfferListResps = Collections.EMPTY_LIST;
        }
        String serviceMobile = "";
        if (ServiceMobileConfig.serviceMobile != null) {
            Random r = new Random();
            serviceMobile = ServiceMobileConfig.serviceMobile.get(r.nextInt(ServiceMobileConfig.serviceMobile.size()));
        }
        String finalServiceMobile = serviceMobile;
        getOfferListResps.stream().forEach(x -> {
            x.setServiceMobile(finalServiceMobile);
        });
        return getOfferListResps;
    }

    public List<GetOfferListResp> getSystemOfferListRespListV2(GetSysQuoListV2Req getSysQuoListV2Req) {
        if (getSysQuoListV2Req.getPgSize() == null) {
            getSysQuoListV2Req.setPgSize(20);
        }
        //        if (getSysQuoListV2Req.getTime() == null) {
        //            getSysQuoListV2Req.setTime(new Timestamp(System.currentTimeMillis()));
        //        }
        List<GetOfferListResp> getOfferListResps = quoteInfoDao
                .getSystemOwnerQuoteListV2(CookieAuthUtils.getCurrentUser(), getSysQuoListV2Req.getTime(),
                        getSysQuoListV2Req.getPgSize());
        if (getOfferListResps == null) {
            getOfferListResps = Collections.EMPTY_LIST;
        }
        String serviceMobile = "";
        if (ServiceMobileConfig.serviceMobile != null) {
            Random r = new Random();
            serviceMobile = ServiceMobileConfig.serviceMobile.get(r.nextInt(ServiceMobileConfig.serviceMobile.size()));
        }
        String finalServiceMobile = serviceMobile;
        getOfferListResps.stream().forEach(x -> {
            x.setServiceMobile(finalServiceMobile);
        });
        return getOfferListResps;

    }

    public List<GetOfferListResp> getSystemOfferListRespList(String quoteId, Integer pgSize) {
        if (pgSize == null) {
            pgSize = 10;
        }
        List<GetOfferListResp> getOfferListResps = quoteInfoDao
                .getSystemOwnerQuoteList(CookieAuthUtils.getCurrentUser(), quoteId, pgSize);
        if (getOfferListResps == null) {
            getOfferListResps = Collections.EMPTY_LIST;
        }
        String serviceMobile = "";
        if (ServiceMobileConfig.serviceMobile != null) {
            Random r = new Random();
            serviceMobile = ServiceMobileConfig.serviceMobile.get(r.nextInt(ServiceMobileConfig.serviceMobile.size()));
        }
        String finalServiceMobile = serviceMobile;
        getOfferListResps.stream().forEach(x -> {
            x.setServiceMobile(finalServiceMobile);
        });
        return getOfferListResps;

    }

    public GetOfferInfoResp getOfferInfoResp(String quoteId) {
        String serviceMobile = "";
        if (ServiceMobileConfig.serviceMobile != null) {
            Random r = new Random();
            serviceMobile = ServiceMobileConfig.serviceMobile.get(r.nextInt(ServiceMobileConfig.serviceMobile.size()));
        }
        GetOfferInfoResp getOfferInfoResp = quoteInfoDao.getQuoteInfoById(quoteId);
        getOfferInfoResp.setServiceMobile(serviceMobile);
        return getOfferInfoResp;
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

    /**
     * 构建系统报价的报价详情数据
     *
     * @param quoteListResps
     * @return
     */
    void buildSystemQuoteDetail(List<GerQuoteListResp> quoteListResps) {
        if (CollectionUtils.isEmpty(quoteListResps)) {
            return;
        }
        quoteListResps.stream().forEach(x -> {
            List<GerQuoteListResp.Detail> details = new LinkedList<>();
            details.add(new GerQuoteListResp.Detail("质量标准",
                    Lists.newArrayList(GerQuoteListResp.convertProperties(x.getProperties()))));
            details.add(new GerQuoteListResp.Detail("单价",
                    Lists.newArrayList(x.getQuote().toString() + " /" + x.getUnitPrice())));
            details.add(new GerQuoteListResp.Detail("注意事项", x.getNotice()));
        });
    }

}
