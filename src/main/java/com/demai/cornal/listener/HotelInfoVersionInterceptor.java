/*
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */

package com.demai.cornal.listener;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HotelInfoVersionInterceptor implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(HotelInfoVersionInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return null;
    }
}
