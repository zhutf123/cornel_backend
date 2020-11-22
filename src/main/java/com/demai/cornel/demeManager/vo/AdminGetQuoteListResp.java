package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

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
    /**
     * 业务审核人id
     */
    private String reviewUserId;
    /**
     * 业务审核人
     */
    private String reviewUser;
    /***
     * 业务审核时间
     */
    private String reviewUserTime;
    /***
     * 财务审核时间
     */
    private String financeUserTime;
    /**
     * 业务审核人id
     */
    private String financeUserId;
    /***
     * 财务审核人
     */
    private String financeUser;

}
