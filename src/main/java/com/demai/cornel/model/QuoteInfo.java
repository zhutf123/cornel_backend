package com.demai.cornel.model;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author binz.zhang
 * @Date: 2019-12-31    13:39
 */
@Data public class QuoteInfo {
    /**
     * 自增ID
     */
    private Integer id;
    /**
     * 报价ID
     */
    private String quoteId;

    /**
     * 商品ID
     */
    private Integer commodityId;

    /**
     * 报价金额
     */
    private BigDecimal quote;

    /**
     * 金额单位
     */
    private String unitPrice;

    /**
     * 出货量
     */
    private BigDecimal shipmentWeight;

    /**
     * 出货重量单位
     */
    private String unitWeight;

    /**
     * 出仓时间
     */
    private Timestamp shipmentTime;

    /**
     * 报价生成时间
     */
    private Timestamp createTime;

    /**
     * 报价的状态 1 有效 0 无效
     */
    private Integer status;

    /**
     * 描述
     */
    private String desc;

    /**
     * 过期时间
     */
    private Timestamp expireTime;

    /**
     * 是否是系统报价，1 是系统报价 0 是用户自定义的报价
     */
    private Integer systemFlag;

    /**
     * 是否接受议价 0 不接受议价 1接受议价
     */
    private Integer bargainStatus;

    /**
     * 报价人
     */
    private String userId;
    /**
     * 报价人
     */
    private String userName;

    /**
     * 出仓位置
     */
    private String location;
    /**
     * 出仓位置
     */
    private String towerId;

    /**
     * 联系电话
     */
    private String mobile;

    public static enum QUOTE_STATUS implements IEmus {
        CANCEL(-1, "取消"), REVIEW(1, "审核中"), REVIEW_PASS(2, "审核通过"), REVIEW_REFUSE(3, "审核拒绝");

        private int value;
        private String expr;

        private QUOTE_STATUS(int value, String expr) {
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

    public static enum SYSTEM_STATUS implements IEmus {
        SYSTEM_STATUS(0, "系统报价"), USER_DEFINE(1, "用户自定义的报价");
        private int value;
        private String expr;

        private SYSTEM_STATUS(int value, String expr) {
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