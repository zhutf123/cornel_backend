package com.demai.cornel.purcharse.vo.req;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-02-22    14:47
 */
@Data
public class UpdatePurcahsePriceReq {
    private String purchaseId;
    private BigDecimal beforeOrderPrice;
    private BigDecimal afterOrderPrice;
    private BigDecimal increase;
    private BigDecimal beforeUnitPrice;
    private BigDecimal afterUnitPrice;
}
