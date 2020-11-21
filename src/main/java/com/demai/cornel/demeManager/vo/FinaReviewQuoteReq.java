package com.demai.cornel.demeManager.vo;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

import java.lang.annotation.Documented;
import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-04-08    19:43
 */
@Data public class FinaReviewQuoteReq {
    private String quoteId;
    private String errDesc;
    private Integer operaType;
    private BigDecimal actualPrice;//实际放款金额
    private String startInterest;//起息日

    public static enum OPERA_TYPE implements IEmus {
        APPROVEL(1, "通过审批"), REJECT(2, "拒绝审批");
        private int value;
        private String expr;

        private OPERA_TYPE(int value, String expr) {
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
