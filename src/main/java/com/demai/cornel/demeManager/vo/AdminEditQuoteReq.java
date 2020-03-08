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
        private String unitPrice="元";
        private String unitWeight="吨";
        private String userId;
        private String towerId;
    }

}
