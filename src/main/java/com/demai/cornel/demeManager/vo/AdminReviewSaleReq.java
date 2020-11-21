package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    21:58
 */
@Data public class AdminReviewSaleReq {
    private Integer status;
    private String orderId;
    private String storeId; //库存ID
    private String freightId;//货运ID
    private BigDecimal inCome;//收益
    private String outStartTime;
    private String outEndTime;
    private Integer errCode;
    private String errDesc;
    private Integer shippProcess;//交货顺序 1先付款后交货；2先付款后交货
}
