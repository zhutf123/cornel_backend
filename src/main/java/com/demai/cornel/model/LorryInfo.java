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
import java.sql.Timestamp;
import java.util.Map;

/**
 * Create By zhutf 19-10-6 下午1:04
 */
@Data @NoArgsConstructor @AllArgsConstructor public class LorryInfo implements Serializable {
    private static final long serialVersionUID = -2319454798286728301L;
    private String id;
    private String lorryType;
    private String company;
    private BigDecimal weight;
    private BigDecimal carryWeight;
    private BigDecimal length;
    private BigDecimal width;
    private String buyTime;
    private BigDecimal mileage;
    private String plateNumber;
    private String frameNumber;
    private Integer idType;
    private String idCard;
    private Integer status;
    private Map<String, String> extInfo;
    private String createTime;
    private String operateTime;
    private Integer defaultFlag;
    private String unitWeight;
    private BigDecimal overCarryWeight;
    private String carLiceOwner;
    private String engineNo;
    private String userId;
    private String lorryId;

    public static enum STATUS_ENUE {

        USEING(1, "使用中"), IDLE(2, "可调度"), INVALID(3, "车辆无效-已解绑-已删除");

        private Integer value;
        private String expr;

        private STATUS_ENUE(Integer value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        public Integer getValue() {
            return value;
        }

        public String getExpr() {
            return expr;
        }

    }
}