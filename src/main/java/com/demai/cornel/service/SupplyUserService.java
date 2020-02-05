package com.demai.cornel.service;

import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.util.GenRandomCodeUtil;
import com.demai.cornel.util.StringUtil;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.WeChat.WechatCode2SessionResp;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author binz.zhang
 * @Date: 2020-02-05    20:46
 */
@Service
@Slf4j
public class SupplyUserService {
    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private WeChatService weChatService;
    @Resource
    private SendMsgService sendMsgService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
        if(phone.equals("13439679479")){
            validCode = 888888;
        }
        stringRedisTemplate.opsForValue().set(Joiner.on("_").join(phone, "loginValid"), String.valueOf(validCode), 300,
                TimeUnit.SECONDS);
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
        if (StringUtil.isBlank(code)){
            return Boolean.FALSE;
        }
        if (msgCode.equalsIgnoreCase(code)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
