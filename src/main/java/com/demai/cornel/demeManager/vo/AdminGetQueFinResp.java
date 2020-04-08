package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-04-08    15:11
 */
/*管理员获取烘干塔带宽金额的信息*/
@Data public class AdminGetQueFinResp {
    private BigDecimal price_count;//总的订单金额
    private BigDecimal avg_price;//平均每单金额
    private BigDecimal total_weight;//总的重量
    private Integer order_count;//总的订单数目

    private BigDecimal total_interest;//总的利息金额
    private long days;//贷款总天数
    private BigDecimal avg_interest;//平均每天的利息
}
