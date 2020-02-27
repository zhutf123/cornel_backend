package com.demai.cornel.purcharse.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.demai.cornel.dmEnum.IEmus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
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
//        CANCLE(0, "取消"), UNDER_DEAL(1, "求购中"), DEAL(2, "已成交"), SHELVES(3, "下架");
CANCLE(0, "取消"), UNDER_PRO(1, "审核中"),DEAL(2, "已成交"), SHELVES(3, "下架");

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