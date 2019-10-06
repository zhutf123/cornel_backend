/**
 * Copyright (c) 2019 demai.com. All Rights Reserved.
 */
package com.demai.cornel.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class AclInfo implements Serializable {
    private static final long serialVersionUID = 7697347608311043384L;
    private Long id;
    private String name;
    private String code;
    private String url;
    private Integer module;
    private Integer status;
    private Map<String, String> extInfo;
    private Date createTime;
    private Date operateTime;

}