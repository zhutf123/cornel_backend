/**
 * Copyright (c) 2019 demai.com. All Rights Reserved.
 */
package com.demai.cornel.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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
public class LorryGisInfo implements Serializable {
    private static final long serialVersionUID = 6268583961756110749L;
    private Long id;
    private Long orderId;
    private Long lorryId;
    private Long userId;
    // private geometry --gis ;
    private Integer status;
    private Map<String, String> extInfo;
    private String createTime;
    private String operateTime;

}