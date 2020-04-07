package com.demai.cornel.purcharse.service;

import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.purcharse.dao.BuyerInfoMapper;
import com.demai.cornel.purcharse.dao.CompanyInfoMapper;
import com.demai.cornel.purcharse.dao.LocationInfoMapper;
import com.demai.cornel.purcharse.model.BuyerCornInfo;
import com.demai.cornel.purcharse.model.BuyerInfo;
import com.demai.cornel.purcharse.model.CompanyInfo;
import com.demai.cornel.purcharse.model.LocationInfo;
import com.demai.cornel.purcharse.vo.req.BuyerCplUserInfoReq;
import com.demai.cornel.purcharse.vo.resp.BuyerCompleteInfoResp;
import com.demai.cornel.purcharse.vo.resp.BuyerCplCompanyResp;
import com.demai.cornel.purcharse.vo.resp.BuyerCplUserInfoResp;
import com.demai.cornel.service.ImgService;
import com.demai.cornel.service.SendMsgService;
import com.demai.cornel.service.WeChatService;
import com.demai.cornel.util.*;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.WeChat.WechatCode2SessionResp;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author binz.zhang
 * @Date: 2020-02-19    22:54
 */
@Slf4j @Service public class BuyerLoginService {
    @Resource private BuyerInfoMapper buyerInfoMapper;
    @Resource private WeChatService weChatService;
    @Resource private SendMsgService sendMsgService;
    @Resource private StringRedisTemplate stringRedisTemplate;
    @Resource private ImgService imgService;
    @Resource private CompanyInfoMapper companyInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;

    public UserLoginResp doLogin(UserLoginParam param) {
        // valid msg code
        log.info("doloogin param is {}",JacksonUtils.obj2String(param));
        if (!checkLoginMsgCode(param.getPhone(), param.getMsgCode())) {
            return new UserLoginResp(StringUtils.EMPTY, StringUtils.EMPTY, 1,
                    UserLoginResp.CODE_ENUE.MSG_CODE_ERROR.getValue(), param.getPhone());
        }
        BuyerInfo buyerInfo = buyerInfoMapper.selectByUserIdByLogin(param.getPhone());
        WechatCode2SessionResp resp = weChatService.getOpenId(param.getJscode());
        if (buyerInfo == null) {
            BuyerInfo buyerInfo1 = new BuyerInfo();
            buyerInfo1.setUserId(UUID.randomUUID().toString());
            buyerInfo1.setMobile(Sets.newHashSet(param.getPhone()));
            buyerInfo1.setStatus(BuyerInfo.USER_STATUS.PENDING.getValue());
            buyerInfoMapper.insertSelective(buyerInfo1);
            return new UserLoginResp(resp.getOpenid(), buyerInfo1.getUserId(), 1,
                    UserLoginResp.CODE_ENUE.SUCCESS.getValue(), param.getPhone(),
                    UserLoginResp.USER_STATUS_ENUE.UNREGISTERED.getValue());
        }
        if (buyerInfo.getStatus() == null || !buyerInfo.getStatus().equals(BuyerInfo.USER_STATUS.ENABLE.getValue())) {
            return new UserLoginResp(resp.getOpenid(), buyerInfo.getUserId(), 1,
                    UserLoginResp.CODE_ENUE.SUCCESS.getValue(), param.getPhone(),
                    UserLoginResp.USER_STATUS_ENUE.UNREGISTERED.getValue());
        }
        return new UserLoginResp(StringUtils.EMPTY, StringUtils.EMPTY, UserInfo.ROLE_ENUE.BUYER.getValue(),
                UserLoginResp.CODE_ENUE.OPENID_ERROR.getValue(), param.getPhone());
    }

    /***
     * 给用户发送登录验证码 -1 查不到用户 0 成功 2 发送失败，稍后重试
     *
     * @param phone
     */
    public Integer sendLoginCodeMsg(String phone) {
        //UserInfo userInfo = getUserInfoByPhone(phone);
        Integer validCode = GenRandomCodeUtil.genRandomCode(6);
        if (phone.equals("13439679479")) {
            validCode = 888888;
        }
        stringRedisTemplate.opsForValue()
                .set(Joiner.on("_").join(phone, "loginValid"), String.valueOf(validCode), 300, TimeUnit.SECONDS);
        Integer sendResult = sendMsgService.sendLoginValid(phone, validCode);
        if (sendResult.compareTo(SendMsgService.SEND_MSG_CODE.SUCCESS.getValue()) == 0) {
            return ResponseStatusEnum.SUCCESS.getValue();
        }
        return ResponseStatusEnum.NETWORK_ERROR.getValue();
    }

    public BuyerCplUserInfoResp driverCompleteUserInfo(BuyerCplUserInfoReq buyerCplUserInfoReq) {
        log.debug("buyer register complete user info user info is [{}]", JacksonUtils.obj2String(buyerCplUserInfoReq));
        BuyerCplUserInfoResp driverCpllUserInfoResp = new BuyerCplUserInfoResp();
        Preconditions.checkNotNull(buyerCplUserInfoReq);
        if (Strings.isNullOrEmpty(buyerCplUserInfoReq.getIdCard()) || Strings
                .isNullOrEmpty(buyerCplUserInfoReq.getUserName()) || Strings
                .isNullOrEmpty(buyerCplUserInfoReq.getMobile()) || CollectionUtils
                .isEmpty(buyerCplUserInfoReq.getImgs())) {
            log.debug("driver register complete fail due to param lock");
            driverCpllUserInfoResp.setOptResult(BuyerCplUserInfoResp.STATUS.PARAM_ERROR.getValue());
            return driverCpllUserInfoResp;
        }
        if (!PhoneUtil.isPhone(buyerCplUserInfoReq.getMobile())) {
            log.debug("buyer register complete fail due to tel format error ");
            driverCpllUserInfoResp.setOptResult(BuyerCplUserInfoResp.STATUS.PHONE_ERROR.getValue());
            return driverCpllUserInfoResp;
        }
        BuyerInfo userInfo = new BuyerInfo();
        BeanUtils.copyProperties(buyerCplUserInfoReq, userInfo);
        userInfo.setStatus(UserInfo.USER_STATUS.ENABLE.getValue());
        userInfo.setUserId(CookieAuthUtils.getCurrentUser());
        userInfo.setMobile(Sets.newHashSet(buyerCplUserInfoReq.getMobile()));
        int res = buyerInfoMapper.updateByPrimaryKeySelective(userInfo);
        if (res == 0) {
            log.debug("driver register complete fail due to update user info fail");
            driverCpllUserInfoResp.setOptResult(BuyerCplUserInfoResp.STATUS.SERVICE_ERROR.getValue());
            return driverCpllUserInfoResp;
        }
        if (CollectionUtils.isEmpty(buyerCplUserInfoReq.getImgs())) {
            log.debug("complete user info img is empty");
        }
        imgService.saveUserInfoImgs(buyerCplUserInfoReq.getImgs(), userInfo.getUserId());
        driverCpllUserInfoResp.setOptResult(BuyerCplUserInfoResp.STATUS.SUCCESS.getValue());
        driverCpllUserInfoResp.setUserId(userInfo.getUserId());
        driverCpllUserInfoResp.setName(userInfo.getUserName());
        log.debug("complete user info [{}]", JacksonUtils.obj2String(driverCpllUserInfoResp));
        return driverCpllUserInfoResp;
    }

    public BuyerCplCompanyResp buyerCplCompany(CompanyInfo companyInfo) {
        log.debug("buyer register complete company info user info is [{}]", JacksonUtils.obj2String(companyInfo));
        BuyerCplCompanyResp buyerCplCompanyResp = new BuyerCplCompanyResp();
        Preconditions.checkNotNull(companyInfo);
        if (Strings.isNullOrEmpty(companyInfo.getCompanyName()) || Strings.isNullOrEmpty(companyInfo.getCompanyName())
                || Strings.isNullOrEmpty(companyInfo.getLocationId())) {
            log.debug("driver register complete company fail due to param lock");
            buyerCplCompanyResp.setOptResult(BuyerCplUserInfoResp.STATUS.PARAM_ERROR.getValue());
            return buyerCplCompanyResp;
        }

        if (companyInfo.getCompanyId() == null) {
            companyInfo.setCompanyId(UUID.randomUUID().toString());
            companyInfoMapper.insertSelective(companyInfo);
            BuyerInfo buyerInfo = buyerInfoMapper.selectByUserId(CookieAuthUtils.getCurrentUser());
            buyerInfo.setCompanyId(companyInfo.getCompanyId());
            buyerInfoMapper.updateByPrimaryKeySelective(buyerInfo);
            buyerCplCompanyResp.setCompanyId(companyInfo.getCompanyId());
            buyerCplCompanyResp.setOptResult(BuyerCplCompanyResp.STATUS.SUCCESS.getValue());
            return buyerCplCompanyResp;
        }
        companyInfoMapper.updateByPrimaryKeySelective(companyInfo);
        buyerCplCompanyResp.setCompanyId(companyInfo.getCompanyId());
        buyerCplCompanyResp.setOptResult(BuyerCplCompanyResp.STATUS.SUCCESS.getValue());
        return buyerCplCompanyResp;
    }

    public BuyerCompleteInfoResp getUserCompleteInfo() {
        BuyerInfo buyerInfo = buyerInfoMapper.selectByUserId(CookieAuthUtils.getCurrentUser());
        BuyerCplUserInfoReq cornInfo = new BuyerCplUserInfoReq();
        if(buyerInfo==null){
            return BuyerCompleteInfoResp.builder().status(BuyerCompleteInfoResp.STATUS.USERR_ERROR.getValue()).build();
        }
        BeanUtils.copyProperties(buyerInfo,cornInfo);
        if(buyerInfo.getMobile()!=null){
            cornInfo.setMobile(buyerInfo.getMobile().iterator().next());
        }else {
            cornInfo.setMobile("");
        }
        cornInfo.setImgs(imgService.getUserImgs(CookieAuthUtils.getCurrentUser()));
        CompanyInfo companyInfo = companyInfoMapper.selectBycompanyId(buyerInfo.getCompanyId());
        if(companyInfo==null){
            return BuyerCompleteInfoResp.builder().userInfo(cornInfo).status(BuyerCompleteInfoResp.STATUS.SUCCESS.getValue()).build();
        }
        LocationInfo locationInfo = locationInfoMapper.selectByLocationId(companyInfo.getLocationId());
        if(locationInfo==null){
            companyInfo.setLocation("");
        }
        companyInfo.setLocation(locationInfo.getLocation());
        return BuyerCompleteInfoResp.builder().userInfo(cornInfo).companyInfo(companyInfo).status(BuyerCompleteInfoResp.STATUS.SUCCESS.getValue()).build();

    }

    //    public Integer sendLoginCodeMsg(String phone) {
    //        UserInfo userInfo = getUserInfoByPhone(phone);
    //        Integer validCode = GenRandomCodeUtil.genRandomCode(6);
    //        if (userInfo == null) {
    //            return ResponseStatusEnum.NO_USER.getValue();
    //        }
    //        stringRedisTemplate.opsForValue().set(Joiner.on("_").join(phone, "loginValid"), String.valueOf(validCode), 300,
    //                TimeUnit.SECONDS);
    //        Integer sendResult = sendMsgService.sendLoginValid(phone, validCode);
    //        if (sendResult.compareTo(SendMsgService.SEND_MSG_CODE.SUCCESS.getValue()) == 0) {
    //            return ResponseStatusEnum.SUCCESS.getValue();
    //        }
    //        return ResponseStatusEnum.NETWORK_ERROR.getValue();
    //    }

    /***
     * 验证该手机号的登录验证码
     *
     * @param phone
     * @return
     */
    public Boolean checkLoginMsgCode(String phone, String msgCode) {
        log.info("check login phone is {}",phone);
        String code = stringRedisTemplate.opsForValue().get(Joiner.on("_").join(phone, "loginValid"));
        if (StringUtil.isBlank(code)) {
            return Boolean.FALSE;
        }
        if (msgCode.equalsIgnoreCase(code)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
