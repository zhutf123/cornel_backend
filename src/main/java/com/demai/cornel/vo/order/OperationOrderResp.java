/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.order;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create By tfzhu 2019/12/21 5:04 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationOrderResp implements Serializable {
    private Boolean success;
    private String orderId;
    private Long orderStatus;
    /***
     *  DELIVERY_RESP_STATUS_ENUE
     *  SUPPLIER_RESP_STATUS_ENUE
     */
    private Integer opResult;
    /***
     * 操作完成时间
     *
     */
    private String sendOutTime;

    private Long realWeight;


    public static enum DELIVERY_RESP_STATUS_ENUE {

        SUCCESS(0, "操作成功"),
        OPERATION_ERROR(1, "订单状态错误-司机未确认订单完成"),
        OPERATION_FALSE(2, "操作失败");
        private int value;
        private String expr;

        private DELIVERY_RESP_STATUS_ENUE(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        public int getValue() {
            return value;
        }

        public String getExpr() {
            return expr;
        }
    }

    public static enum SUPPLIER_RESP_STATUS_ENUE {

        SUCCESS(0, "操作成功"),
        OPERATION_ERROR(1, "订单状态错误-司机未确认装车"),
        OPERATION_FALSE(2, "操作失败");
        private int value;
        private String expr;

        private SUPPLIER_RESP_STATUS_ENUE(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        public int getValue() {
            return value;
        }

        public String getExpr() {
            return expr;
        }
    }

}
