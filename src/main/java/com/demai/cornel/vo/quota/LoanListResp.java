package com.demai.cornel.vo.quota;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author binz.zhang
 * @Date: 2020-03-17    17:58
 */
public class LoanListResp  {
    private BigDecimal actualPrice;

    private Timestamp applyTime;

    /**
     * 放款日期
     */
    private Timestamp lendingTime;
}
