/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.service;

import com.demai.cornel.constant.ConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Create By zhutf 19-10-6 下午7:20
 */
@Service
public class WeChatService {

    private final Logger logger = LoggerFactory.getLogger(WeChatService.class);

    @Resource
    private ConfigProperties configProperties;

    @Resource
    private RestTemplate restTemplate;

    public void code2Session() {
        String result = null;
        try {
            result = restTemplate.getForObject(configProperties.weChatCode2SessionPath, String.class);
            logger.info("======={}", result);
        } catch (Exception e) {
            logger.error("==========", e);
        }
    }

}
