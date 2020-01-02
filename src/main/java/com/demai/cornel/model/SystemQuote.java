package com.demai.cornel.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
* @Author binz.zhang
* @Date: 2020-01-02    13:09
*/
@Data
public class SystemQuote {
    /**
    * 自增ID
    */
    private Integer id;

    /**
    * 报价ID
    */
    private String quoteId;

    /**
    * 商品ID
    */
    private String commodityId;

    /**
    * 价格
    */
    private BigDecimal quote;

    /**
    * 价格单位  元/吨
    */
    private String unitPrice;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Integer status;

}