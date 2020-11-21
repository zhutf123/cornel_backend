/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.extern.slf4j.Slf4j;

/**
 * Create By zhutf 18-10-21 下午4:31
 */
@Component
@Slf4j
public class DosomethingJob {
    private static final Logger logger = LoggerFactory.getLogger(DosomethingJob.class);

//    @XxlJob(value = "dosomethingJob")
//    public void execute(String s) throws Exception {
//        logger.info("XXL-JOB Hello World===========");
//    }
}
