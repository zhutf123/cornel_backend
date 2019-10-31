package com.demai.cornel.constant;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigProperties implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ConfigProperties.class);

    public @Value("${we-chat.code2session}") String weChatCode2SessionPath;

}
