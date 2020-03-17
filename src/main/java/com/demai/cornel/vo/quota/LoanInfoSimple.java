package com.demai.cornel.vo.quota;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author binz.zhang
 * @Date: 2020-03-17    10:57
 */
@Data
public class LoanInfoSimple {

    /**
     * 贷款金额
     */
    private BigDecimal price;
    /**
     * 贷款人
     */
    private String applyUserId;

    /**
     * 请款日期
     */
    private String applyTime;

    /**
     * 放款日期
     */
    private String lendingTime;


    /**
     * 状态0 已取消 1待审核 2审核通过 3审核拒绝 4待放款 5已放款 6
     */
    private Integer status;
    private BigDecimal actualPrice;
}
