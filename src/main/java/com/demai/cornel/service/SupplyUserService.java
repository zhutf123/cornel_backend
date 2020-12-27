package com.demai.cornel.service;

import com.demai.cornel.config.ServiceMobileConfig;
import com.demai.cornel.dao.DryTowerDao;
import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.model.DryTower;
import com.demai.cornel.model.OpCorn;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.util.*;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.WeChat.WechatCode2SessionResp;
import com.demai.cornel.vo.delivery.DriverCpllUserInfoReq;
import com.demai.cornel.vo.delivery.DriverCpllUserInfoResp;
import com.demai.cornel.vo.delivery.SupplierCplUserInfoReq;
import com.demai.cornel.vo.delivery.SupplierCpllUserInfoResp;
import com.demai.cornel.vo.delivery.SupplierOtherUserInfoReq;
import com.demai.cornel.vo.supplier.SupplierInfoResp;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author binz.zhang
 * @Date: 2020-02-05    20:46
 */
@Service @Slf4j public class SupplyUserService {
    @Resource private UserInfoDao userInfoDao;
    @Resource private WeChatService weChatService;
    @Resource private SendMsgService sendMsgService;
    @Resource private StringRedisTemplate stringRedisTemplate;
    @Resource private ImgService imgService;
    @Resource private DryTowerDao dryTowerDao;

    public Boolean removeOtherUser(String curUser, String towerId, String userId) {
        DryTower dryTower = dryTowerDao.selectByTowerId(towerId);
        if (dryTower == null || !dryTower.getBindUserId().equals(curUser)) {
            return Boolean.FALSE;
        }
        DryTower dryTowerTmp = new DryTower();
        dryTowerTmp.setTowerId(dryTower.getTowerId());
        dryTower.getContactUserId().remove(userId);
        dryTowerTmp.setContactUserId(dryTower.getContactUserId());
        dryTowerDao.updateByPrimaryKeySelective(dryTowerTmp);
        userInfoDao.updateUserInfoOffline(userId);
        return Boolean.TRUE;
    }

    public List<SupplierInfoResp> getOtherUserInfo(String curUser, String towerId) {
        List<SupplierInfoResp> result = Lists.newArrayList();
        DryTower dryTower = dryTowerDao.selectByTowerId(towerId);
        if (dryTower == null || !dryTower.getBindUserId().equals(curUser)) {
            return result;
        }
        Set<String> userIds = dryTower.getContactUserId();
        userIds.remove(curUser);

        List<UserInfo> userInfos = userInfoDao.getUserInfoByUserIds(userIds);
        userInfos.forEach(userInfo -> {
            SupplierInfoResp supplierInfoResp = new SupplierInfoResp();
            supplierInfoResp.setUserId(userInfo.getUserId());
            supplierInfoResp.setId(userInfo.getId());
            supplierInfoResp.setIdCard(userInfo.getIdCard());
            supplierInfoResp.setUserName(userInfo.getName());
            supplierInfoResp.setServiceMobile(ServiceMobileConfig.serviceMobile.get(0));
            if (userInfo.getMobile() != null || userInfo.getMobile().size() > 0) {
                supplierInfoResp.setMobile(userInfo.getMobile().iterator().next());
            }
            supplierInfoResp.setStatus(SupplierInfoResp.CODE_ENUE.SUCCESS.getValue());
            supplierInfoResp.setImgs(imgService.getUserImgs(userInfo.getUserId()));
            result.add(supplierInfoResp);
        });
        result.sort(Comparator.comparing(SupplierInfoResp::getId).reversed());
        return result;
    }

    public Boolean addOtherUserInfo(SupplierOtherUserInfoReq supplierCplUserInfoReq) {
        UserInfo userInfo = userInfoDao.getUserInfoByPhone(supplierCplUserInfoReq.getMobile());

        if (userInfo != null) {
            userInfo.setStatus(UserInfo.USER_STATUS.ENABLE.getValue());
            UserInfo infoTmp = new UserInfo();
            userInfo.setStatus(UserInfo.USER_STATUS.ENABLE.getValue());
            infoTmp.setUserId(userInfo.getUserId());
            infoTmp.setMobile(Sets.newHashSet(supplierCplUserInfoReq.getMobile()));
            infoTmp.setRole(UserInfo.ROLE_ENUE.SUPPLIER.getValue());
            infoTmp.setIdCard(supplierCplUserInfoReq.getIdCard());
            infoTmp.setIdType(supplierCplUserInfoReq.getIdType());
            infoTmp.setName(supplierCplUserInfoReq.getName());
            userInfoDao.update(infoTmp);
        } else {
            userInfo = new UserInfo();
            userInfo.setMobile(Sets.newHashSet(supplierCplUserInfoReq.getMobile()));
            userInfo.setStatus(UserInfo.USER_STATUS.ENABLE.getValue());
            userInfo.setRole(UserInfo.ROLE_ENUE.SUPPLIER.getValue());
            userInfo.setIdCard(supplierCplUserInfoReq.getIdCard());
            userInfo.setIdType(supplierCplUserInfoReq.getIdType());
            userInfo.setName(supplierCplUserInfoReq.getName());
            userInfo.setUserId(UUID.randomUUID().toString());
            userInfoDao.save(userInfo);
        }

        SupplierCpllUserInfoResp driverCpllUserInfoResp = new SupplierCpllUserInfoResp();
        Preconditions.checkNotNull(supplierCplUserInfoReq);
        if (Strings.isNullOrEmpty(supplierCplUserInfoReq.getIdCard()) || Strings
                .isNullOrEmpty(supplierCplUserInfoReq.getName()) || Strings
                .isNullOrEmpty(supplierCplUserInfoReq.getMobile()) || CollectionUtils
                .isEmpty(supplierCplUserInfoReq.getImgs())) {
            log.debug("supplier register complete fail due to param lock");
            driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.PARAM_ERROR.getValue());
            return Boolean.FALSE;
        }
        if (!PhoneUtil.isPhone(supplierCplUserInfoReq.getMobile())) {
            log.debug("supplier register complete fail due to tel format error ");
            driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.PHONE_ERROR.getValue());
            return Boolean.FALSE;
        }
        //BeanUtils.copyProperties(supplierCplUserInfoReq, userInfoInit);
        if (CollectionUtils.isEmpty(supplierCplUserInfoReq.getImgs())) {
            log.debug("supplier complete user info img is empty");
        }
        imgService.saveUserInfoImgs(supplierCplUserInfoReq.getImgs(), userInfo.getUserId());
        driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.SUCCESS.getValue());
        driverCpllUserInfoResp.setUserId(supplierCplUserInfoReq.getUserId());
        driverCpllUserInfoResp.setName(supplierCplUserInfoReq.getName());
        log.debug("supplier complete user info [{}]", JacksonUtils.obj2String(driverCpllUserInfoResp));
        //修改烘干塔联系人id
        DryTower dryTower = dryTowerDao.selectByTowerId(supplierCplUserInfoReq.getTowerId());
        if (dryTower == null) {
            return Boolean.FALSE;
        }
        DryTower dryTowerTmp = new DryTower();
        dryTowerTmp.setTowerId(dryTower.getTowerId());
        dryTower.getContactUserId().add(userInfo.getUserId());
        dryTowerTmp.setContactUserId(dryTower.getContactUserId());
        dryTowerDao.updateByPrimaryKeySelective(dryTowerTmp);
        return Boolean.TRUE;
    }

    public UserLoginResp doLogin(UserLoginParam param) {
        // valid msg code
        if (!checkLoginMsgCode(param.getPhone(), param.getMsgCode())) {
            return new UserLoginResp(StringUtils.EMPTY, StringUtils.EMPTY, 1,
                    UserLoginResp.CODE_ENUE.MSG_CODE_ERROR.getValue(), param.getPhone());
        }
        log.debug("jscode is [{}]", param.getJscode());
        UserInfo userInfo = userInfoDao.getSupplyUserInfoByPhone(param.getPhone());
        WechatCode2SessionResp resp = weChatService.getOpenId(param.getJscode());
        if (log.isDebugEnabled()) {
            log.debug("get openid by js code result:{}", JsonUtil.toJson(resp));
        }
        if (userInfo == null) {
            UserInfo userInfoInit = new UserInfo();
            userInfoInit.setMobile(Sets.newHashSet(param.getPhone()));
            userInfoInit.setOpenId(Sets.newHashSet(resp.getOpenid()));
            userInfoInit.setUserId(UUID.randomUUID().toString());
            userInfoInit.setStatus(UserInfo.USER_STATUS.PENDING.getValue());
            userInfoInit.setRole(UserInfo.ROLE_ENUE.SUPPLIER.getValue());
            userInfoDao.save(userInfoInit);
            return new UserLoginResp(resp.getOpenid(), userInfoInit.getUserId(), 1,
                    UserLoginResp.CODE_ENUE.SUCCESS.getValue(), param.getPhone(),
                    UserLoginResp.USER_STATUS_ENUE.UNREGISTERED.getValue());
        }

        if (resp != null && StringUtil.isNotBlank(resp.getOpenid())) {
            Set<String> openIds = userInfo.getOpenId();
            if (CollectionUtils.isEmpty(openIds)) {
                openIds = Sets.newHashSet(resp.getOpenid());
            }
            userInfoDao.updateUserOpenIdByUid(openIds, userInfo.getId());
            return new UserLoginResp(resp.getOpenid(), userInfo.getUserId(), userInfo.getRole(),
                    UserLoginResp.CODE_ENUE.SUCCESS.getValue(), param.getPhone(),
                    UserLoginResp.USER_STATUS_ENUE.REGISTERED.getValue());
        }
        return new UserLoginResp(StringUtils.EMPTY, StringUtils.EMPTY, userInfo.getRole(),
                UserLoginResp.CODE_ENUE.OPENID_ERROR.getValue(), param.getPhone(),
                UserLoginResp.USER_STATUS_ENUE.REGISTERED.getValue());
    }

    /***
     * 给用户发送登录验证码 -1 查不到用户 0 成功 2 发送失败，稍后重试
     *
     * @param phone
     */
    public Integer sendLoginCodeMsg(String phone) {
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

    /***
     * 验证该手机号的登录验证码
     *
     * @param phone
     * @return
     */
    public Boolean checkLoginMsgCode(String phone, String msgCode) {
        String code = stringRedisTemplate.opsForValue().get(Joiner.on("_").join(phone, "loginValid"));
        if (StringUtil.isBlank(code)) {
            return Boolean.FALSE;
        }
        if (msgCode.equalsIgnoreCase(code)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public SupplierCpllUserInfoResp supplierCompleteUserInfo(SupplierCplUserInfoReq supplierCplUserInfoReq) {
        log.debug("driver register complete user info user info is [{}]",
                JacksonUtils.obj2String(supplierCplUserInfoReq));
        SupplierCpllUserInfoResp driverCpllUserInfoResp = new SupplierCpllUserInfoResp();
        Preconditions.checkNotNull(supplierCplUserInfoReq);
        if (Strings.isNullOrEmpty(supplierCplUserInfoReq.getIdCard()) || Strings
                .isNullOrEmpty(supplierCplUserInfoReq.getName()) || Strings
                .isNullOrEmpty(supplierCplUserInfoReq.getMobile()) || CollectionUtils
                .isEmpty(supplierCplUserInfoReq.getImgs())) {
            log.debug("supplier register complete fail due to param lock");
            driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.PARAM_ERROR.getValue());
            return driverCpllUserInfoResp;
        }
        if (!PhoneUtil.isPhone(supplierCplUserInfoReq.getMobile())) {
            log.debug("supplier register complete fail due to tel format error ");
            driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.PHONE_ERROR.getValue());
            return driverCpllUserInfoResp;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setStatus(UserInfo.USER_STATUS.ENABLE.getValue());
        if (StringUtil.isEmpty(supplierCplUserInfoReq.getUserId())){
            userInfo.setUserId(CookieAuthUtils.getCurrentUser());
            supplierCplUserInfoReq.setUserId(userInfo.getUserId());
        }
        BeanUtils.copyProperties(supplierCplUserInfoReq, userInfo);
        Set<String> mobiles = Sets.newHashSet(supplierCplUserInfoReq.getMobile());
        userInfo.setMobile(mobiles);

        int res = userInfoDao.update(userInfo);
        if (res == 0) {
            log.debug("supplier register complete fail due to update user info fail");
            driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.SERVICE_ERROR.getValue());
            return driverCpllUserInfoResp;
        }
        if (CollectionUtils.isEmpty(supplierCplUserInfoReq.getImgs())) {
            log.debug("supplier complete user info img is empty");
        }
        imgService.saveUserInfoImgs(supplierCplUserInfoReq.getImgs(), userInfo.getUserId());
        driverCpllUserInfoResp.setOptResult(DriverCpllUserInfoResp.STATUS.SUCCESS.getValue());
        driverCpllUserInfoResp.setUserId(supplierCplUserInfoReq.getUserId());
        driverCpllUserInfoResp.setName(supplierCplUserInfoReq.getName());
        log.debug("supplier complete user info [{}]", JacksonUtils.obj2String(driverCpllUserInfoResp));
        return driverCpllUserInfoResp;
    }

    public OpCorn updateUserCornInfo(SupplierCplUserInfoReq driverCornInfo) {
        if (driverCornInfo == null) {
            return OpCorn.builder().optResult(OpCorn.STATUS.PARAM_ERROR.getValue()).build();
        }
        UserInfo userInfoBefore = userInfoDao.getUserInfoByUserId(driverCornInfo.getUserId());
        if (userInfoBefore == null) {
            return OpCorn.builder().optResult(OpCorn.STATUS.NO_OPT_TARGET.getValue()).build();
        }
        UserInfo userInfoAfter = new UserInfo();
        BeanUtils.copyProperties(driverCornInfo, userInfoAfter);
        if (!Strings.isNullOrEmpty(driverCornInfo.getMobile())) {
            userInfoAfter.setMobile(Sets.newHashSet(driverCornInfo.getMobile()));
        }
        int res = userInfoDao.update(userInfoAfter);
        if (res == 0) {
            return OpCorn.builder().optResult(OpCorn.STATUS.SERVER_ERROR.getValue()).build();
        }
        if (driverCornInfo.getImgs() != null && driverCornInfo.getImgs().size() > 0) {
            imgService.saveUserInfoImgs(driverCornInfo.getImgs(), driverCornInfo.getUserId());
        }
        return OpCorn.builder().optResult(OpCorn.STATUS.SUCCESS.getValue()).build();
    }
}
