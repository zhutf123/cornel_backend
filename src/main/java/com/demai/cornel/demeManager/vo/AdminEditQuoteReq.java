package com.demai.cornel.demeManager.vo;

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
        private Integer systemFlag; // 0 或者null 为针对烘干塔的报价 1是系统报价
    }

}
