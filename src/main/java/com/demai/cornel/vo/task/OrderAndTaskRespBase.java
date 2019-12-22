/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.task;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Create By tfzhu  2019/12/22  3:15 PM
 */
@Data
public class OrderAndTaskRespBase extends  TaskInfoRespBase {
    private String verifyCode;
    private String serviceMobile;
    private String orderWeight;
    private String startTime;

    /**烘干塔出货重量*/
    private String orderCarryWeight;
    /**烘干塔出货时间*/
    private BigDecimal sendOutTime;

    /**接货人收获重量*/
    private String succWeight;
    /**接货人收获时间*/
    private BigDecimal deliveryReceiveTime;

    private Long orderStatus;
    private Integer orderStatusDesc;

    private BigDecimal unitWeightPrice;
    private String unitPrice = "元";
    private String unitWeight = "吨";
    private String unitDistance = "km";


    public static enum STATUS_DESC_ENUE {

        NORMAL(0, "正常"),
        DELAY(1, "延迟");

        private Integer value;
        private String expr;

        private STATUS_DESC_ENUE(Integer value, String expr) {
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
