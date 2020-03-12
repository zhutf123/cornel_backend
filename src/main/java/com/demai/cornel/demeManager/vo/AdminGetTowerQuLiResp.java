package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-03-12    10:53
 */
@Data
public class AdminGetTowerQuLiResp {
    private String commodityName;
    private BigDecimal quote;
    private String unitPrice;
    private String unitWeight;
}
