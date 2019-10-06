/**
 * Copyright (c) 2019 demai.com. All Rights Reserved.
 */
package com.demai.cornel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

/**
 * Create By zhutf 19-10-6 下午1:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo implements Serializable {
    private static final long serialVersionUID = 5140939699074828658L;
    private Long id;
    private Long taskId;
    private Long lorryId;
    private Long userId;

    private BigDecimal distance;

    private String unit;

    private BigDecimal carryWeight;

    private BigDecimal orderWeight;

    private BigDecimal succWeight;

    private Integer overweight;
    private Date acceptTime;
    private Date startTime;
    private Date mustFinishTime;
    private Date sendOutTime;
    private Date estimateFinishTime;
    private Date finishTime;
    private String sendOutCode;
    private Integer sendOutUserId;
    private String receiveCode;
    private Integer receiverUserId;
    private Integer status;
    private String unexpect;
    private Date cancelTime;
    private String cancelReason;
    private Map<String, String> extInfo;
    private Date createTime;
    private Date operateTime;

}