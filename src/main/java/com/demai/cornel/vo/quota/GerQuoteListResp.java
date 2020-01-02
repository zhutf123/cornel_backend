package com.demai.cornel.vo.quota;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-01-02    10:59
 */
@Data
public class GerQuoteListResp {
    private String quoteId;
    private String commodityId;
    private String quote;
    private String unitPrice;
    private String unitWeight;
}
