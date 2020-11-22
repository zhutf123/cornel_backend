package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    21:00
 */
@Data
public class AdminGetSyQuLis {
    private String commodityId;
    private String commodityName;
    private BigDecimal quote;
    private BigDecimal sysQuote;
    private String unitPrice;
    private String unitWeight;
}
