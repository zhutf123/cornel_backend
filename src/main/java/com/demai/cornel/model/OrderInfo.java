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
import java.util.Set;

/**
 * Create By zhutf 19-10-6 下午1:10
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class OrderInfo implements Serializable {
    private static final long serialVersionUID = 5140939699074828658L;
    private Long id;
    private String orderId;
    private String taskId;
    private String lorryId;
    private String userId;

    private BigDecimal distance;
    private String unitDistance;

    private BigDecimal carryWeight;

    private BigDecimal orderWeight;

    private BigDecimal succWeight;

    private Integer overweight;
    private String unitWeight;

    private String acceptTime;
    private String startTime;
    private String mustFinishTime;
    private String sendOutTime;
    private String estimateFinishTime;
    private String finishTime;
    private String sendOutCode;
    private Set<String> sendOutUserId;
    private String receiveCode;
    private Set<String> receiverUserId;
    private Long status;
    private String unexpect;
    private String cancelTime;
    private String cancelReason;
    private Map<String, String> extInfo;
    private String createTime;
    private String operateTime;
    private String receiveTime;
    private String deliveryReceiveTime;
    private String letOutTime;

    private Long oldStatus;

    public static enum STATUS_ENUE {

        ORDER_INIT(1L, "待收粮"),
        ORDER_CANCEL(1L << 1, "取消订单"),
        ORDER_ARRIVE_DEP(1L << 2,"到达装货点"),
        SUPPLIER_CONFIRM_SHIPMENT(1L << 3, "烘干塔开始装货"),
        ORDER_SHIPMENT(1L << 4,"司机确认-装货完成"),
        ORDER_SHIPMENT_OVER(1L << 5, "烘干塔-装货信息录入确认"),
        ORDER_ROUTING((1L << 6), "司机确认装货完成-运粮中"),

        ORDER_ARRIVE_ARR((1L << 9), "已送达"),
        ORDER_DELIVERY((1L << 10), "卸粮中"),
        ORDER_DRIVER_CONFIRM_OVER((1L << 11) ,"司机确认-卸粮完成"),
        ORDER_DELIVERY_OVER((1L << 12) , "接货人-卸粮信息录入确认"),
                
        ORDER_SUCCESS((1L << 13) , "司机确认卸粮完成-订单完成"),
        ORDER_CUSTOMER((1L << 14) , "订单人工处理");

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

}