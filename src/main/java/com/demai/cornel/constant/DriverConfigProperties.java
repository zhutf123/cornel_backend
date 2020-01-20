package com.demai.cornel.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author binz.zhang
 * @Date: 2020-01-20    17:48
 */
@Component
public class DriverConfigProperties {
    /**
     * 获取openid 请求接口
     */
    public @Value("${we-chat-driver.code2session}") String weChatCode2SessionPath;

    /**
     * 小程序 appId
     */
    public @Value("${we-chat-driver.appId}") String appId;

    /**
     * 小程序 appSecret
     */
    public @Value("${we-chat-driver.appSecret}") String appSecret;

    /**
     * 阿里api访问 id
     */
    public @Value("${ali-config.accessKeyId}") String AliAccessKeyId;
    /**
     * 阿里api访问  secret
     */
    public @Value("${ali-config.accessSecret}") String AliAccessSecret;
}
