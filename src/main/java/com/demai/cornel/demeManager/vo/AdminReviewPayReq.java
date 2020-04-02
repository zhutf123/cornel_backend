package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-04-02    12:29
 */
@Data public class AdminReviewPayReq {
    private String orderId;
    private BigDecimal actualPay;
    private String payReview;
    private String payTime;
    private String exInfo;
}
