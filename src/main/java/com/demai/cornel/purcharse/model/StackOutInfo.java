package com.demai.cornel.purcharse.model;

import java.math.BigDecimal;
import java.util.Date;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    14:56
 */
@Data public class StackOutInfo {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 商品ID
     */
    private String commodityId;

    /**
     * 出货ID
     */
    private String outId;

    /**
     * 出货操作人员
     */
    private String operatorUser;

    /**
     * 买家ID
     */
    private String buyerId;

    /**
     * 审核员
     */
    private String reviewUserId;

    /**
     * 运费
     */
    private BigDecimal freightPrice;

    /**
     * 订单价格
     */
    private BigDecimal orderPrice;

    /**
     * 价格单位
     */
    private String unitPrice;

    /**
     * 出货量
     */
    private BigDecimal weight;

    /**
     * 重量单位
     */
    private String unitWeight;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 出货开始时间
     */
    private Date startTime;

    /**
     * 出货截止时间
     */
    private Date endTime;

    /**
     * 发货地
     */
    private String fromLocation;

    /**
     * 收货地点
     */
    private String receiveLocation;

    /**
     * 库存ID
     */
    private String storeId;

    /**
     * 运单ID
     */
    private String waybillId;

    /**
     * 付款方式
     */
    private String paymentType;

    /**
     * 付款状态0 待付款 1已付款
     */
    private Integer paymentStatus;

    /**
     * 货品ID
     */
    private String cargoId;

    private Integer status;

    private String freightInfoId;

    public static enum STATUS_ENUM implements IEmus {
        //        CANCLE(0, "取消"),
        //        UNDER_APPROVAL(1<<0, "待审核"),
        //        UNDER_DELIVER(1<<1, "待运输"),
        //        DELIVER_ING(1<<2, "运输中"),
        //        UNDER_RECEIVE(1<<3, "待接货"),
        //        FINISH(5, "订单完成");
        CANCLE(0, "取消"), UNDER_APPROVAL(1 << 0, "待审核"), REJECT_APPROVAL(1 << 1, "审核拒绝"), PASS_APPROVAL(1 << 2, "审核通过");
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