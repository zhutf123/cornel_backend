package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    10:43
 */
/*管理员获取待支付订单的list*/
@Data
public class AdminUnpaySaleList extends  AdminGetSaleList {
    private String fromLocation;// 发货地
    private String transportType;//货运方式
    private BigDecimal esInCome;//收益
    private String reviewUser;//收益

}
