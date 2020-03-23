package com.demai.cornel.model;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-01-06    11:58
 */
@Data
public class QuoteInfo {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 商品ID
     */
    private String commodityId;

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
    private Timestamp startTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 报价的状态 1 有效 0 无效
     */
    private Integer status;

    /**
     * 描述
     */
    private String desc;

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
     * 出仓位置
     */
    private String location;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 报价ID
     */
    private String quoteId;

    /**
     * 烘干塔ID
     */
    private String towerId;

    /**
     * 联系电话
     */
    private String mobile;
    private String reviewUser;
    private Timestamp warehouseTime;

    private Integer cargoStatus;// 货物状态
    private BigDecimal wetWeight;//湿粮重量
    private Set<String> loanId;//贷款ID

    private HashMap<String,String>reviewOpt;
    /**
     * 结束时间
     */
    private Timestamp endTime;
    public static enum SYSTEM_STATUS implements IEmus {
        SYSTEM(1, "参数错误"), USER_DEFINE(0, "用户发起订单");

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
    public static enum QUOTE_TATUS implements IEmus {
        CANCEL(-1, "取消"), REVIEW(1, "审核中"),
        REVIEW_PASS(2, "审核通过"), REVIEW_REFUSE(3, "审核拒绝");
        private int value;
        private String expr;

        private QUOTE_TATUS(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return value;
        }

        @Override public String getExpr() {
            return expr;
        }

    }public static enum CARGO_STATUS implements IEmus {
        futures(2, "等待货物入库"), spot(1, "现货");
        private int value;
        private String expr;

        private CARGO_STATUS(int value, String expr) {
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