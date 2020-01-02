package com.demai.cornel.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2019-12-31    17:08
 */
@Data
public class OfferCommodityQuoteReq {
    private String userName;
    private String userId;
    private Commodity commodityId;
    private BigDecimal shipmentWeight;
    private String unitWeight;
    private String location;
    private String startTime;
    private String endTime;
}
