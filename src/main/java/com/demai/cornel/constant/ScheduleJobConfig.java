///**
// * Copyright (c) 2019 dm.com. All Rights Reserved.
// */
//package com.demai.cornel.constant;
//
//import com.xxl.job.core.executor.XxlJobExecutor;
//import lombok.extern.slf4j.Slf4j;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.handler.IJobHandler;
//import com.xxl.job.core.handler.annotation.JobHandler;
//
///**
// * Create By zhutf 18-10-21 下午4:31
// */
//@Configuration
//@Slf4j
//@ComponentScan(basePackages = "com.demai.cornel.job")
//public class ScheduleJobConfig {
//    private static final Logger logger = LoggerFactory.getLogger(ScheduleJobConfig.class);
//
//    @Value("${xxl.job.admin.addresses}")
//    private String adminAddresses;
//
//    @Value("${xxl.job.executor.appname}")
//    private String appName;
//
//    @Value("${xxl.job.executor.ip}")
//    private String ip;
//
//    @Value("${xxl.job.executor.port}")
//    private int port;
//    @Value("${xxl.job.executor.logpath}")
//    private String logPath;
//
//    @Value("${xxl.job.executor.logretentiondays}")
//    private int logRetentionDays;
//
//    @Value("${xxl.job.accessToken}")
//    private String accessToken;
//
//    @Bean(initMethod = "start", destroyMethod = "destroy")
//    public XxlJobExecutor xxlJobExecutor() {
//        logger.info(">>>>>>>>>>> cornel-job config init.{}-{}-{}-{}-{}-{}-{}", adminAddresses, appName, ip, port,
//                accessToken, logPath, logRetentionDays);
//        XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
//        xxlJobExecutor.setAdminAddresses(adminAddresses);
//        xxlJobExecutor.setAppName(appName);
//        xxlJobExecutor.setIp(ip);
//        xxlJobExecutor.setPort(port);
//        xxlJobExecutor.setAccessToken(accessToken);
//        xxlJobExecutor.setLogPath(logPath);
//        xxlJobExecutor.setLogRetentionDays(logRetentionDays);
//        return xxlJobExecutor;
//    }
//}
