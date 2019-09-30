/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.conver;

import javax.annotation.PostConstruct;

import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Component;

/**
 * Create By zhutf 19-9-26 下午11:59
 */
@Component
public class CustomXmlMessageConverter extends MappingJackson2XmlHttpMessageConverter {
    @PostConstruct
    void init() {

    }
}
