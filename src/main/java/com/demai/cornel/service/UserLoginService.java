/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.service;

import javax.annotation.Resource;

import com.demai.cornel.dmEnum.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.WeChat.WechatCode2SessionResp;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;

/**
 * Create By zhutf 19-10-31 下午2:51
 */
@Service
@Slf4j
public class UserLoginService {

    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private WeChatService weChatService;
    @Resource
    private SendMsgService sendMsgService;

    public UserLoginResp doLogin(UserLoginParam param) {
        // valid msg code
        if (!checkLoginMsgCode(param.getPhone())) {
            return new UserLoginResp(StringUtils.EMPTY, StringUtils.EMPTY, 0,
                    UserLoginResp.CODE_ENUE.MSG_CODE_ERROR.getValue());
        }

        UserInfo userInfo = userInfoDao.getUserInfoByPhone(param.getPhone());
        if (userInfo == null) {
            return new UserLoginResp(StringUtils.EMPTY, StringUtils.EMPTY, 0,
                    UserLoginResp.CODE_ENUE.NO_USER.getValue());
        }

        WechatCode2SessionResp resp = weChatService.getOpenId(param.getJscode());
        if (resp != null && resp.getErrcode() == WechatCode2SessionResp.CODE_ENUE.SUCCESS.getValue()) {
            if (log.isDebugEnabled()) {
                log.debug("get openid by js code result:{}", JsonUtil.toJson(resp));
            }
            //to do update user info phone valid and openId

            return new UserLoginResp(resp.getOpenid(), userInfo.getUserId(), 0,
                    UserLoginResp.CODE_ENUE.SUCCESS.getValue());
        }
        return new UserLoginResp(StringUtils.EMPTY, StringUtils.EMPTY, 0,
                UserLoginResp.CODE_ENUE.OPENID_ERROR.getValue());
    }

    /***
     * 给用户发送登录验证码 -1 查不到用户 0 成功 2 发送失败，稍后重试
     * 
     * @param phone
     */
    public Integer sendLoginCodeMsg(String phone) {
        UserInfo userInfo = getUserInfoByPhone(phone);
        if (userInfo == null) {
            return ResponseStatusEnum.NO_USER.getValue();
        }
        String validCode = "";
        //to do save code to redis
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
    public Boolean checkLoginMsgCode(String phone) {
        // to do get code from msg redis
        return Boolean.TRUE;
    }

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    public UserInfo getUserInfoByPhone(String phone) {
        return userInfoDao.getUserInfoByPhone(phone);
    }

}
