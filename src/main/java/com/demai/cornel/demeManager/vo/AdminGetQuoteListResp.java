package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    13:30
 */
@Data
public class AdminGetQuoteListResp {
    private BigDecimal shipmentWeight;
    private BigDecimal quote;
    /**
     /**
     *系统提供报价
     */
    private BigDecimal sysQuote;
    private String unitWeight;
    private String unitPrice;
    private String commodityId;
    private String commodityName;
    private String warehouseTime;//货物入库时间

    /**
     * 用户名
     */
    private String userName;
    /**
     * 报价ID
     */
    private String quoteId;
    private String mobile;
    private Integer status;
    private String company;//公司名称

}
