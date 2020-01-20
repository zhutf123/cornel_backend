package com.demai.cornel.service;

import com.demai.cornel.constant.ConfigProperties;
import com.demai.cornel.constant.DriverConfigProperties;
import com.demai.cornel.vo.WeChat.WechatCode2SessionResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Author binz.zhang
 * @Date: 2020-01-20    17:47
 */
@Service
@Slf4j
public class WeChatDriverService {
    @Resource
    private DriverConfigProperties driverConfigProperties;

    @Resource
    private RestTemplate restTemplate;

    public WechatCode2SessionResp getOpenId(String jsCode) {
        WechatCode2SessionResp result = null;
        try {
            result = restTemplate.getForObject(driverConfigProperties.weChatCode2SessionPath, WechatCode2SessionResp.class,
                    driverConfigProperties.appId, driverConfigProperties.appSecret, jsCode);
        } catch (Exception e) {
            log.error("通过用户登录信息获取 用户的openId", e);
        }
        return result;
    }
}
