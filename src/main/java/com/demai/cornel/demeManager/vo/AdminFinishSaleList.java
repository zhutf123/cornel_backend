package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    10:44
 */
/*管理员获取已完成订单list*/
@Data public class AdminFinishSaleList extends AdminGetSaleList {

    private String fromLocation;// 发货地
    private String transportType;//货运方式
    private BigDecimal esInCome;//收益
    private String reviewUser;//收益

    private String payReview;//财务确认人
    private String payTime;//付款时间
    private BigDecimal actualPay;//实际付款金额
    private String exInfo;//其他信息

}
