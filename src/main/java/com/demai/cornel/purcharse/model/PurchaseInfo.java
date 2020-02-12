package com.demai.cornel.purcharse.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:29
 ** 买家求购信息
*/
@Data
public class PurchaseInfo {
    /**
    * 自增ID
    */
    private Integer id;

    /**
    * 求购商品
    */
    private String commodityId;

    /**
    * 买家ID
    */
    private String userId;

    /**
    * 单价
    */
    private BigDecimal price;

    /**
    * 求购数量
    */
    private BigDecimal weight;

    /**
    * 价格单位
    */
    private String unitPrice;

    /**
    * 重量单位
    */
    private String unitWeight;

    /**
    * 收货地址
    */
    private String locationId;

    /**
    * 收货起始时间
    */
    private Timestamp receiveStart;

    /**
    * 收货截止时间
    */
    private Timestamp receiveEnd;

    /**
    * 0 无效 1有效
    */
    private Short status;

    /**
    * 创建时间
    */
    private Timestamp createTime;


}