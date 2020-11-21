package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    10:05
 */
@Data public class AdminGetSaleListResp {
    private Integer status;
    private BigDecimal totalPrice;
    private BigDecimal totalWeight;
    private Integer orderNum;
}
