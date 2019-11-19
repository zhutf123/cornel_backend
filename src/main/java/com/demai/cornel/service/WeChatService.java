/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.service;

import com.demai.cornel.constant.ConfigProperties;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.WeChat.WechatCode2SessionResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Create By zhutf 19-10-6 下午7:20
 */
@Service
@Slf4j
public class WeChatService {

    @Resource
    private ConfigProperties configProperties;

    @Resource
    private RestTemplate restTemplate;

    public WechatCode2SessionResp getOpenId(String jsCode) {
        WechatCode2SessionResp result = null;
        try {
            result = restTemplate.getForObject(configProperties.weChatCode2SessionPath, WechatCode2SessionResp.class,
                    configProperties.appId, configProperties.appSecret, jsCode);
        } catch (Exception e) {
            log.error("通过用户登录信息获取 用户的openId", e);
        }
        return result;
    }

}
