package com.demai.cornel.demeManager.vo;

import lombok.Data;

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
        private String quote;
        private String unitPrice;
        private String unitWeight;
        private String userId;
        private String towerId;
    }

}
