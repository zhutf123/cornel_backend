package com.demai.cornel.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import lombok.Data;

/**
* @Author binz.zhang
* @Date: 2020-03-17    09:49
*/
@Data
public class LoanInfo {
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
}