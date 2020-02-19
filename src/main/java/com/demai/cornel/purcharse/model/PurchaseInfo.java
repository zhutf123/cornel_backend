package com.demai.cornel.purcharse.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-18    21:31
 */
@Data public class PurchaseInfo {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 求购商品
     */
    private String commodityId;

    /**
     * 买家ID
     */
    private String buyerId;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 求购数量
     */
    private BigDecimal weight;

    /**
     * 价格单位
     */
    private String unitPrice;

    /**
     * 重量单位
     */
    private String unitWeight;

    /**
     * 收货地址
     */
    private String receiveLocationId;

    /**
     * 收货起始时间
     */
    private Timestamp receiveStartTime;

    /**
     * 收货截止时间
     */
    private Timestamp receiveEndTime;

    /**
     * 0 无效 1有效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 联系人
     */
    private String contactUserName;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 求购ID
     */
    private String purchaseId;
    public static enum STATUS_ENUM implements IEmus {
        CANCLE(0, "取消"),
        UNDER_APPROVAL(1, "待审核"),
        UNDER_DELIVER(2, "待运输"),
        UNDER_RECEIVE(3, "带接货"),
        FINISH(5, "订单完成");

        private int value;
        private String expr;

        private STATUS_ENUM(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return value;
        }

        @Override public String getExpr() {
            return expr;
        }

    }
}