/**
 * Copyright (c) 2019 demai.com. All Rights Reserved.
 */
package com.demai.cornel.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create By zhutf 19-10-6 下午1:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOperationLog implements Serializable {
    private static final long serialVersionUID = 6310419168693806006L;
    private Long id;
    private Long orderId;
    private String mark;
    private Long operator;
    private Map<String, String> extInfo;
    private Date createTime;
    private Date operateTime;

}