/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.service;

import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.util.StringUtil;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.WeChat.WechatCode2SessionResp;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    public UserLoginResp doLogin(UserLoginParam param) {
        UserInfo userInfo = userInfoDao.getUserInfoByPhone(param.getPhone());
        if (userInfo == null) {
            return new UserLoginResp(StringUtils.EMPTY, StringUtils.EMPTY, 0,
                    UserLoginResp.CODE_ENUE.NO_USER.getValue());
        }
        WechatCode2SessionResp resp = weChatService.getOpenId(param.getJscode());
        if (resp != null && resp.getErrcode() == WechatCode2SessionResp.CODE_ENUE.SUCCESS.getValue()) {
            if (log.isDebugEnabled()) {
                log.debug("get openid by jscode result:{}", JsonUtil.toJson(resp));
            }
            return new UserLoginResp(resp.getOpenid(), userInfo.getUserId(), 0,
                    UserLoginResp.CODE_ENUE.SUCCESS.getValue());
        }
        return new UserLoginResp(StringUtils.EMPTY, StringUtils.EMPTY, 0,
                UserLoginResp.CODE_ENUE.OPENID_ERROR.getValue());
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
