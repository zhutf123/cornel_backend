/*
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */

package com.demai.cornel.listener;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestInterceptor implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(TestInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return null;
    }
}
