package com.demai.cornel.constant;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component public class ConfigProperties implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ConfigProperties.class);

    /**
     * 获取openid 请求接口
     */
    public @Value("${we-chat.code2session}") String weChatCode2SessionPath;

    /**
     * 小程序 appId
     */
    public @Value("${we-chat.appId}") String appId;

    /**
     * 小程序 appSecret
     */
    public @Value("${we-chat.appSecret}") String appSecret;

    /**
     * 阿里api访问 id
     */
    public @Value("${ali-config.accessKeyId}") String AliAccessKeyId;
    /**
     * 阿里api访问  secret
     */
    public @Value("${ali-config.accessSecret}") String AliAccessSecret;
    /**
     * 发送登陆验证码模版id
     */
    public @Value("${ali-config.loginValidcodeId}") String loginValidcodeId;

    /**
     * 发送价格变化短息模版id
     */
    public @Value("${ali-config.priceChangeCodeId}") String priceChangeCodeId;

    /**
     * 发送价格变化短息模版id
     */
    public @Value("${ali-config.notifyBusinessOp}") String notifyBusinessOp;

    /**
     * 发送价格变化短息模版id
     */
    public @Value("${ali-config.notifyFinanceOp}") String notifyFinanceOp;

    public @Value("${upload.location}") String uploadLocation;
    public @Value("${upload.downloadHost}") String downloadHost;


    public @Value("${cookie.domain}") String cookieDomain;

}
