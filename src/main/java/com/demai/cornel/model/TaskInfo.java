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

    private String unit;

    private BigDecimal unacceptWeight;
    private BigDecimal undistWeight;

    private Date startTime;
    private Date endTime;
    private Date unitCostTime;
    private String dep;
    private String arr;

    private BigDecimal distance;

    private String unitDistance;

    private BigDecimal unitPrice;

    private BigDecimal estimatePrice;

    private Integer level;
    private Integer status;
    private Map<String, String> extInfo;
    private Date createTime;
    private Date operateTime;
    private Map<String, Integer> subTaskTime;
    private String subTaskTimeString;
    private Set<String> receiverUserId;
    private Set<String> sendOutUserId;

    public void setSubTaskTimeString(String subTaskTimeString) {
        if (Strings.isNullOrEmpty(subTaskTimeString)) {
            this.subTaskTimeString = subTaskTimeString;
            this.subTaskTime = (HashMap<String, Integer>) Collections.EMPTY_MAP;
        }
        List<SubTaskTime> list = JSON.parseArray(subTaskTimeString, SubTaskTime.class);//将json字符串转化成list
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