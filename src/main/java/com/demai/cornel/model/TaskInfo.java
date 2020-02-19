/**
 * Copyright (c) 2019 2019 demai.com. All Rights Reserved.
 */
package com.demai.cornel.model;

import com.alibaba.fastjson.JSON;
import com.demai.cornel.util.JacksonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Create By zhutf 19-10-6 下午1:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class TaskInfo implements Serializable {
    private static final long serialVersionUID = 3690760337103210363L;
    private Long id;
    private String title;
    private String taskId;
    private String product;


    private BigDecimal weight;

    private String unitWeight;

    private BigDecimal unacceptWeight;
    private BigDecimal undistWeight;

    private String startTime;
    private String endTime;
    private String unitCostTime;
    private String dep;
    private String arr;
    private String depGis;
    private String arrGis;

    private BigDecimal distance;

    private String unitDistance;

    private BigDecimal unitWeightPrice;

    private BigDecimal estimatePrice;

    private Integer level;
    private Long status;
    private Map<String, String> extInfo;
    private String createTime;
    private String operateTime;
    private Map<String, Integer> subTaskTime;
    private String subTaskTimeString;
    private Set<String> receiverUserId;
    private Set<String> sendOutUserId;
    private String unitPrice;
    private Set<String> receiverMobile;
    private Set<String> supplierMobile;

    public static enum STATUS_ENUE {

        TASK_INIT(1L, "添加任务"), TASK_CANCEL(1L << 1, "取消任务"), TASK_REVIEW_DENY(
                1L << 3, "审核拒绝"),

        TASK_ING(1L << 4, "任务进行中"),

        TASK_OVER(1L << 5, "任务完成"),

        TASK_CUSTOMER((1L << 6), "任务异常-人工处理");

        private long value;
        private String expr;

        private STATUS_ENUE(long value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        public long getValue() {
            return value;
        }

        public String getExpr() {
            return expr;
        }

    }

    public void setSubTaskTimeString(String subTaskTimeString) {
        if (Strings.isNullOrEmpty(subTaskTimeString)) {
            this.subTaskTimeString = subTaskTimeString;
            this.subTaskTime = (HashMap<String, Integer>) Collections.EMPTY_MAP;
        }
        List<SubTaskTime> list = JSON.parseArray(subTaskTimeString, SubTaskTime.class);// 将json字符串转化成list
        this.subTaskTime = (list == null) ? (HashMap<String, Integer>) Collections.EMPTY_MAP
                : list.stream().collect(Collectors.toMap(SubTaskTime::getTime, SubTaskTime::getCount));
        this.subTaskTimeString = subTaskTimeString;
    }

    @Data
    public static class SubTaskTime {
        private String time;
        private Integer count;

    }

}