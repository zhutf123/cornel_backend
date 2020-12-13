package com.demai.cornel.demeManager.vo;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    20:40
 */
@Data
public class AdminEditQuoteReq {
    public List<quoteInfo> quoteInfos;

    @Data
    public static class quoteInfo{
        private String commodityId;
        private BigDecimal quote;
        private BigDecimal selfQuote;
        private String unitPrice="元";
        private String unitWeight="吨";
        private String userId;
        private String towerId;
        private Integer systemFlag;
    }

    /***
     * 资源报价类型
     */
    public static enum QUOTE_FLAG implements IEmus {
        SPECIAL_FLAG(0, "针对烘干塔报价"),
        SYSTEM_FLAG(1 , "系统全局报价");

        private int value;
        private String expr;

        QUOTE_FLAG(int value, String expr) {
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
