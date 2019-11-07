/**
 * Copyright (c) 2019 2019 demai.com. All Rights Reserved.
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
 * Create By zhutf 19-10-6 下午1:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private BigDecimal unitPrice;

    private BigDecimal estimatePrice;

    private Integer level;
    private Integer status;
    private Map<String, String> extInfo;
    private Date createTime;
    private Date operateTime;

}