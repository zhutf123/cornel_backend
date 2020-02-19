package com.demai.cornel.purcharse.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-18    21:51
 */
@Data public class SaleOrder {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 货品ID
     */
    private String cargoId;

    /**
     * 买家ID
     */
    private String buyerId;

    /**
     * 下单时间
     */
    private Timestamp orderTime;

    /**
     * 预计送达起始时间
     */
    private Timestamp receiveStartTime;

    /**
     * 付款方式
     */
    private String paymentType;

    /**
     * 付款状态 0 待支付 1支付完成
     */
    private Integer paymentStatus;

    /**
     * 运费
     */
    private BigDecimal freightPrice;

    /**
     * 商品价格
     */
    private BigDecimal commodityPrice;

    /**
     * 订单的总金额=freigh_price+commodity_price
     */
    private BigDecimal orderPrice;

    /**
     * 价格单位
     */
    private String unitPrice;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 重量单位
     */
    private String unitWeight;

    /**
     * 运单列表
     */
    private String waybillId;

    /**
     * 对应的求购信息 为空则说明是购买的系统挂出的订单
     */
    private String purchaseId;

    /**
     * 联系人
     */
    private String contactUserName;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 收货地址
     */
    private String receiveLocation;

    /**
     * 发货地
     */
    private String fromLocation;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 报价ID
     */
    private String offerId;

    /**
     * 预计收货截止时间
     */
    private Timestamp receiveEndTime;


    private String commodityId;

    public static enum STATUS_ENUM implements IEmus {
        CANCLE(0, "取消"),
        UNDER_APPROVAL(1<<0, "待审核"),
        UNDER_DELIVER(1<<1, "待运输"),
        DELIVER_ING(1<<2, "运输中"),
        UNDER_RECEIVE(1<<3, "待接货"),
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