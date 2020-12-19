package com.demai.cornel.service;

import com.demai.cornel.config.ServiceMobileConfig;
import com.demai.cornel.constant.ConfigProperties;
import com.demai.cornel.constant.ContextConsts;
import com.demai.cornel.dao.CommodityDao;
import com.demai.cornel.dao.DryTowerDao;
import com.demai.cornel.dao.LoanInfoMapper;
import com.demai.cornel.dao.QuoteInfoDao;
import com.demai.cornel.dao.SystemQuoteDao;
import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.demeManager.dao.ReviewLogMapper;
import com.demai.cornel.demeManager.dao.SpecialQuoteMapper;
import com.demai.cornel.demeManager.model.SpecialQuote;
import com.demai.cornel.model.BargainRange;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.model.DryTower;
import com.demai.cornel.model.ImgInfoReq;
import com.demai.cornel.model.LoanInfo;
import com.demai.cornel.model.QuoteInfo;
import com.demai.cornel.model.SystemQuote;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.DateFormatUtils;
import com.demai.cornel.util.DateUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.util.StringUtil;
import com.demai.cornel.util.TimeStampUtil;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.quota.ClickSystemQuoteResp;
import com.demai.cornel.vo.quota.GerQuoteListResp;
import com.demai.cornel.vo.quota.GetOfferInfoResp;
import com.demai.cornel.vo.quota.GetOfferListResp;
import com.demai.cornel.vo.quota.GetQuoteListReq;
import com.demai.cornel.vo.quota.GetSysQuoListV2Req;
import com.demai.cornel.vo.quota.LoanInfoSimple;
import com.demai.cornel.vo.quota.OfferQuoteReq;
import com.demai.cornel.vo.quota.OfferQuoteResp;
import com.demai.cornel.vo.quota.SystemQuoteReq;
import com.demai.cornel.vo.quota.SystemQuoteV2Req;
import com.demai.cornel.vo.quota.UserDefineQuoteReq;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.demai.cornel.constant.ContextConsts.MIN_SHIPMENT_WEIGHT;
import static com.demai.cornel.util.DateFormatUtils.ISO_DATETIME_PATTERN;
import static com.demai.cornel.util.DateFormatUtils.ISO_DATE_PATTERN;

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
    @Resource private LoanInfoMapper loanInfoMapper;
    @Resource private ImgService imgService;
    @Resource private OpterReviewService opterReviewService;
    @Resource
    private SendMsgService sendMsgService;
    @Resource
    private ConfigProperties configProperties;

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
        quoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.UNDER_SER_REVIEW.getValue());
        quoteInfo.setQuoteId(UUID.randomUUID().toString());
        quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(ISO_DATE_PATTERN, offerQuoteReq.getStartTime()));
        quoteInfo.setEndTime(TimeStampUtil.stringConvertTimeStamp(ISO_DATE_PATTERN, offerQuoteReq.getEndTime()));
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
        quoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.UNDER_SER_REVIEW.getValue());
        quoteInfo.setQuoteId(UUID.randomUUID().toString());
        quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(ISO_DATE_PATTERN, offerQuoteReq.getStartTime()));
        quoteInfo.setEndTime(TimeStampUtil.stringConvertTimeStamp(ISO_DATE_PATTERN, offerQuoteReq.getEndTime()));
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
        quoteInfo.setSysQuote(offerQuoteReq.getSysQuote());
        if (Strings.isNullOrEmpty(offerQuoteReq.getMobile())) {
            UserInfo userInfo = userInfoDao.getUserInfoByUserId(CookieAuthUtils.getCurrentUser());
            if (!CollectionUtils.isEmpty(userInfo.getMobile())) {
                quoteInfo.setMobile(userInfo.getMobile().iterator().next());
            }
        }
        sendNotifyToOp(SendMsgService.SEND_MSG_TYPE.BUS_OP, dryTower.getCompany());
        quoteInfo.setSystemFlag(QuoteInfo.SYSTEM_STATUS.SYSTEM.getValue());
        quoteInfo.setUserName(userInfoDao.getUserNameByUserId(userId));
        quoteInfo.setStatus(QuoteInfo.QUOTE_TATUS.UNDER_SER_REVIEW.getValue());
        quoteInfo.setQuoteId(UUID.randomUUID().toString());
        quoteInfo.setStartTime(TimeStampUtil.stringConvertTimeStamp(ISO_DATE_PATTERN, offerQuoteReq.getStartTime()));
        quoteInfo.setEndTime(TimeStampUtil.stringConvertTimeStamp(ISO_DATE_PATTERN, offerQuoteReq.getEndTime()));
        quoteInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        quoteInfo.setWarehouseTime(getWarehouseTime(offerQuoteReq.getWarehouseTime()));
        log.info("save qute info:{}", JsonUtil.toJson(quoteInfo));
        quoteInfoDao.insertSelective(quoteInfo);
        
        offerQuoteResp.setStatus(OfferQuoteResp.STATUS_ENUE.SUCCESS.getValue());
        offerQuoteResp.setQuoteStatus(quoteInfo.getStatus());
        offerQuoteResp.setQuoteId(quoteInfo.getQuoteId());
        return offerQuoteResp;
    }

    public void sendNotifyToOp(SendMsgService.SEND_MSG_TYPE opType,String company){
        Set<String> phones = Sets.newHashSet();
        String key = null;
        List<UserInfo> userInfos = null;
        switch (opType){
        case BUS_OP:
            userInfos = userInfoDao.getBusOpUserInfo();
            key = configProperties.notifyBusinessOp;
            break;
        case FIN_OP:
            userInfos = userInfoDao.getFinanceOpUserInfo();
            key = configProperties.notifyFinanceOp;
            break;
        }
        
        if (CollectionUtils.isNotEmpty(userInfos)){
            userInfos.stream().forEach(user ->phones.addAll(user.getMobile()));
        }
        
        if (CollectionUtils.isNotEmpty(phones)){
            sendMsgService.sendNotifyMsgToOp(phones, key, company);
        }
    }

    public Timestamp getWarehouseTime(String warehouseDate) {
        Date date = DateUtils.now();
        int hour = date.getHours();
        int min = date.getMinutes();
        int sec = date.getSeconds();
        Timestamp warehouseTime = new Timestamp(TimeStampUtil
                .stringConvertTimeStamp(ISO_DATETIME_PATTERN, warehouseDate + " " + hour + ":" + min + ":" + sec)
                .getTime());
        return warehouseTime;

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
                shipmentWeight(MIN_SHIPMENT_WEIGHT).unitPrice(ContextConsts.DEFAULT_UNIT_PRICE)
                .unitWeight(ContextConsts.DEFAULT_UNIT_WEIGHT).build();

    }

    /**
     * 获取系统商品报价
     *
     * @param getQuoteListReq
     * @return
     */
    public List<GerQuoteListResp> getSystemQuoteList(GetQuoteListReq getQuoteListReq) {
        List<GerQuoteListResp> gerQuoteListResp = systemQuoteDao.getNewSystemQuote(getQuoteListReq.getQuoteId(),
                Optional.ofNullable(getQuoteListReq.getPgSize()).orElse(10));
        if (CollectionUtils.isEmpty(gerQuoteListResp)) {
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
            gerQuoteListResp.stream().forEach(x -> {
                if (finalBigDecimalHashMap.get(x.getCommodityId()) != null) {
                    x.setQuote(finalBigDecimalHashMap.get(x.getCommodityId()));
                }
            });
        }
        buildSystemQuoteDetail(gerQuoteListResp);
        return gerQuoteListResp;

    }

    private boolean checkQuote(OfferQuoteReq offerQuoteReq) {
        return offerQuoteReq != null && offerQuoteReq.getCommodityId() != null && !Strings
                .isNullOrEmpty(offerQuoteReq.getTowerId()) && offerQuoteReq.getShipmentWeight() != null
                && offerQuoteReq.getQuote() != null;
    }

    /**
     * 获取议价的范围
     *
     * @return
     */
    public BargainRange getBargainRange() {
        return BargainRange.builder().upper(6).down(6).unit(5).build();
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
        SpecialQuote specialQuote = specialQuoteMapper.selectSpecialQuoteByCommodityId(userId,commodityId);
        clickSystemQuoteResp.setCommodity(commodity);
//        clickSystemQuoteResp.setShipmentWeight(
//                systemQuote.getMinShipmentWeight() != null ? systemQuote.getMinShipmentWeight() : MIN_SHIPMENT_WEIGHT);
        clickSystemQuoteResp.setUnitWeight(systemQuote.getUnitWeight());
        clickSystemQuoteResp.setUnitPrice(systemQuote.getUnitPrice());
        clickSystemQuoteResp.setQuote(systemQuote.getQuote());
        clickSystemQuoteResp.setSysQuote(systemQuote.getQuote());
        if (specialQuote != null){
            //clickSystemQuoteResp.setShipmentWeight(specialQuote.getMinShipmentWeight());
            clickSystemQuoteResp.setUnitWeight(specialQuote.getUnitWeight());
            clickSystemQuoteResp.setUnitPrice(specialQuote.getUnitPrice());
            clickSystemQuoteResp.setQuote(specialQuote.getQuote());
            clickSystemQuoteResp.setSysQuote(specialQuote.getQuote());
        }

        clickSystemQuoteResp.setStatus(ClickSystemQuoteResp.STATUS_ENUE.SUCCESS.getValue());
        List<ClickSystemQuoteResp.DryTowerInfo> dryTowerInfo = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ownDryInfo)) {
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
        List<GetOfferListResp> getOfferListResp = quoteInfoDao
                .getOwnerQuoteList(CookieAuthUtils.getCurrentUser(), quoteId, pgSize);
        if (CollectionUtils.isEmpty(getOfferListResp)) {
            return Collections.EMPTY_LIST;
        }
        String serviceMobile = "";
        if (ServiceMobileConfig.serviceMobile != null) {
            Random r = new Random();
            serviceMobile = ServiceMobileConfig.serviceMobile.get(r.nextInt(ServiceMobileConfig.serviceMobile.size()));
        }
        String finalServiceMobile = serviceMobile;
        getOfferListResp.stream().forEach(x -> {
            x.setReviewInfo(opterReviewService.towerReviewConvert(x.getReviewOpt()));
            x.setServiceMobile(finalServiceMobile);
        });
        return getOfferListResp;
    }

    public List<GetOfferListResp> getSystemOfferListRespListV2(GetSysQuoListV2Req getSysQuoListV2Req) {
        if (getSysQuoListV2Req.getPgSize() == null) {
            getSysQuoListV2Req.setPgSize(20);
        }
        List<GetOfferListResp> getOfferListResps = quoteInfoDao
                .getSystemOwnerQuoteListV2(CookieAuthUtils.getCurrentUser(), getSysQuoListV2Req.getTime(),
                        getSysQuoListV2Req.getPgSize());
        if (CollectionUtils.isEmpty(getOfferListResps)) {
            getOfferListResps = Collections.EMPTY_LIST;
        }
        String serviceMobile = "";
        if (ServiceMobileConfig.serviceMobile != null) {
            Random r = new Random();
            serviceMobile = ServiceMobileConfig.serviceMobile.get(r.nextInt(ServiceMobileConfig.serviceMobile.size()));
        }
        String finalServiceMobile = serviceMobile;

        getOfferListResps.stream().forEach(x -> {
            if (x.getLoanId() != null) {
                List<LoanInfo> loanInfos = loanInfoMapper.getLoanByLoanIds(x.getLoanId());
                if (loanInfos != null && loanInfos.iterator().next().getStatus()
                        .equals(LoanInfo.STATUS.PROVER.getValue())) {
                    x.setShowLoan(0);//先统一关闭
                    LoanInfoSimple loanInfoSimple = new LoanInfoSimple();
                    BeanUtils.copyProperties(loanInfos.iterator().next(), loanInfoSimple);
                    loanInfoSimple.setApplyTime(loanInfos.iterator().next().getApplyTime() == null ?
                            null :
                            TimeStampUtil
                                    .timeStampConvertString(ISO_DATE_PATTERN, loanInfos.iterator().next().getApplyTime()));
                    loanInfoSimple.setLendingTime(loanInfos.iterator().next().getLendingTime() == null ?
                            null :
                            TimeStampUtil
                                    .timeStampConvertString(ISO_DATE_PATTERN, loanInfos.iterator().next().getLendingTime()));
                    x.setLoanInfo(Lists.newArrayList(loanInfoSimple));
                } else {
                    x.setLoanInfo(Collections.EMPTY_LIST);
                }
            }
            x.setChangeLog(buildChangeLog(x.getFrontValue(), x));
            QuoteInfo.REVEW_STATUS viewStatus = QuoteInfo.REVEW_STATUS.getViewStatusByValue(x.getStatus());
            x.setStatusDesc(viewStatus != null ? viewStatus.getExpr() : "");
            x.setReviewInfo(opterReviewService.towerReviewConvert(x.getReviewOpt()));
            x.setServiceMobile(finalServiceMobile);
        });
        return getOfferListResps;

    }

    /***
     * 构建订单信息修改内容
     */
    public List<QuoteInfo.ChangeLogInfo> buildChangeLog(String frontValue, GetOfferListResp quoteInfo) {
        List<QuoteInfo.ChangeLogInfo> result = Lists.newArrayList();
        if (!quoteInfo.getStatus().equals(QuoteInfo.QUOTE_TATUS.SER_REVIEW_PASS.getValue())){
            return result;
        }
        if (StringUtil.isNotEmpty(frontValue)) {
            QuoteInfo oldQuote = JsonUtil.fromJson(frontValue, QuoteInfo.class);
            if(!(oldQuote.getQuote().compareTo(quoteInfo.getQuote()) == 0)){
                result.add(QuoteInfo.ChangeLogInfo.builder()
                        .frontValue(oldQuote.getQuote())
                        .value(quoteInfo.getQuote())
                        .desc("报价金额发生变化")
                        .code("quote")
                        .build());
            }

            if(!(oldQuote.getShipmentWeight().compareTo(quoteInfo.getShipmentWeight()) == 0)){
                result.add(QuoteInfo.ChangeLogInfo.builder()
                        .frontValue(oldQuote.getShipmentWeight())
                        .value(quoteInfo.getShipmentWeight())
                        .desc("出货量发生变化")
                        .code("weight")
                        .build());
            }

            String startTime = DateFormatUtils.formatDateTime(oldQuote.getStartTime());
            if (!startTime.equals(quoteInfo.getStartTime())) {
                result.add(QuoteInfo.ChangeLogInfo.builder().
                        frontValue(oldQuote.getShipmentWeight())
                        .value(quoteInfo.getShipmentWeight())
                        .desc("入库时间发生变化")
                        .code("time")
                        .build());
            }

        }
        return result;
    }

    public List<GetOfferListResp> getSystemOfferListRespList(String quoteId, Integer pgSize) {
        if (pgSize == null) {
            pgSize = 10;
        }
        List<GetOfferListResp> getOfferListResp = quoteInfoDao
                .getSystemOwnerQuoteList(CookieAuthUtils.getCurrentUser(), quoteId, pgSize);
        if (CollectionUtils.isEmpty(getOfferListResp)) {
            return Collections.EMPTY_LIST;
        }
        String serviceMobile = "";
        if (ServiceMobileConfig.serviceMobile != null) {
            Random r = new Random();
            serviceMobile = ServiceMobileConfig.serviceMobile.get(r.nextInt(ServiceMobileConfig.serviceMobile.size()));
        }
        String finalServiceMobile = serviceMobile;
        getOfferListResp.stream().forEach(x -> {
            x.setServiceMobile(finalServiceMobile);
        });
        return getOfferListResp;

    }

    public GetOfferInfoResp getOfferInfoResp(String quoteId) {
        String serviceMobile = "";
        if (ServiceMobileConfig.serviceMobile != null) {
            Random r = new Random();
            serviceMobile = ServiceMobileConfig.serviceMobile.get(r.nextInt(ServiceMobileConfig.serviceMobile.size()));
        }

        GetOfferInfoResp getOfferInfoResp = quoteInfoDao.getQuoteInfoById(quoteId);
        if (getOfferInfoResp.getLoanId() != null && getOfferInfoResp.getLoanId().size() > 0) {
            List<LoanInfo> loanInfos = loanInfoMapper.getLoanByLoanIds(getOfferInfoResp.getLoanId());
            if (loanInfos != null) {
                getOfferInfoResp.setLoanInfo(loanInfos.stream().map(x -> {
                    LoanInfoSimple loanInfoSimple = new LoanInfoSimple();
                    BeanUtils.copyProperties(x, loanInfoSimple);
                    loanInfoSimple.setApplyTime(x.getApplyTime() == null ?
                            null :
                            TimeStampUtil.timeStampConvertString(ISO_DATE_PATTERN, x.getApplyTime()));
                    loanInfoSimple.setLendingTime(x.getLendingTime() == null ?
                            null :
                            TimeStampUtil.timeStampConvertString(ISO_DATE_PATTERN, x.getLendingTime()));
                    return loanInfoSimple;
                }).collect(Collectors.toList()));
                getOfferInfoResp.setLoanPrice(loanInfos.get(0).getPrice());
            } else {
                getOfferInfoResp.setLoanInfo(Collections.EMPTY_LIST);
            }
        }
        QuoteInfo.REVEW_STATUS viewStatus = QuoteInfo.REVEW_STATUS.getViewStatusByValue(getOfferInfoResp.getStatus());
        getOfferInfoResp.setStatusDesc(viewStatus != null ? viewStatus.getExpr() : "");
        getOfferInfoResp.setReviewInfo(opterReviewService.towerReviewConvert(getOfferInfoResp.getReviewOpt()));
        List<ImgInfoReq> imgInfoReqs = imgService.getQuoteImgs(quoteId);
        getOfferInfoResp.setImgInfo(imgInfoReqs);
        getOfferInfoResp.setServiceMobile(serviceMobile);
        buildChangeLog(getOfferInfoResp.getFrontValue(), getOfferInfoResp);
        List<DryTower> ownDryInfo = dryTowerDao.selectDryTowerByUserId(CookieAuthUtils.getCurrentUser());
        List<ClickSystemQuoteResp.DryTowerInfo> dryTowerInfo = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(ownDryInfo)) {
            ownDryInfo.stream().forEach(x -> {
                dryTowerInfo.add(new ClickSystemQuoteResp.DryTowerInfo(String.valueOf(x.getTowerId()), x.getLocation(),
                        x.getLocationArea(), x.getLocationDetail()));
            });
        }
        getOfferInfoResp.setDryTowerInfo(dryTowerInfo);

        return getOfferInfoResp;
    }

    public OfferQuoteResp cancelQuote(String quoteId) {
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
            Date d1 = DateFormatUtils.parse(start);//定义起始日期
            Date d2 = DateFormatUtils.parse(end);//定义结束日期
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
            x.setDetail(details);
        });
    }

}
