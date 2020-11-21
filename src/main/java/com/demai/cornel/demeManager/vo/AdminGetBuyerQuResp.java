package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-04-17    14:19
 */
@Data
public class AdminGetBuyerQuResp {

    private String commodityName;
    private BigDecimal quote;
    private String unitPrice;
    private String unitWeight;
    private String commodityId;

}
