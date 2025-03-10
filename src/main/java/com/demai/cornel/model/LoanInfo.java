package com.demai.cornel.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-03-17    09:49
 */
@Data public class LoanInfo {
    /**
     * 自增ID
     */
    private Integer id;

    private String loanId;

    /**
     * 贷款金额
     */
    private BigDecimal price;

    /**
     * 利息
     */
    private BigDecimal interest;

    /**
     * 贷款人
     */
    private String applyUserId;

    /**
     * 请款日期
     */
    private Timestamp applyTime;

    /**
     * 放款日期
     */
    private Timestamp lendingTime;

    /**
     * 预计还款日期
     */
    private Timestamp estRepayTime;

    /**
     * 实际还款日期
     */
    private Timestamp actualRepayTime;

    /**
     * 利息总和
     */
    private BigDecimal interestTotal;

    /**
     * 还款金额
     */
    private BigDecimal repayPrice;

    /**
     * 审批人
     */
    private String reviewUserId;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 状态0 已取消 1待审核 2审核通过 3审核拒绝
     */
    private Integer status;

    private BigDecimal actualPrice;//实际放款金额

    private Timestamp startInterest;// 计算利息的日期

    public static enum STATUS implements IEmus {
        CANCEL(0, "取消"), UNDER_REVIEW(1, "待审核"), PROVER(2, "审核通过"), REJECT(3, "审核拒绝");
        private int value;
        private String expr;

        private STATUS(int value, String expr) {
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