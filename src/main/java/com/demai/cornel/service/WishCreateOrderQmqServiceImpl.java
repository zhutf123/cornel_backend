package com.demai.cornel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("wishCreateOrderQmqService")
public class WishCreateOrderQmqServiceImpl  {
    private final Logger logger = LoggerFactory.getLogger(WishCreateOrderQmqServiceImpl.class);

    public static final String PROPERITY_NAME = "data";

    public void createOrderByQmq( ) {

    }
}
