/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.job;

import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;

/**
 * Create By zhutf 18-10-21 下午4:31
 */
@Component
@Slf4j
@JobHandler("dosomethingJob")
public class DosomethingJob extends IJobHandler {
    private static final Logger logger = LoggerFactory.getLogger(DosomethingJob.class);


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        System.out.println("XXL-JOB Hello World");
        return SUCCESS;
    }
}
