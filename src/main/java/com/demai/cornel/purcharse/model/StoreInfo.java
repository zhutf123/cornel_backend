package com.demai.cornel.purcharse.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    13:08
 */
@Data public class StoreInfo {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 库存ID
     */
    private String commodityId;

    /**
     * 位置ID
     */
    private String locationId;

    /**
     * 购买价格
     */
    private BigDecimal buyingPrice;

    /**
     * 价格单位
     */
    private String unitPrice;

    /**
     * 库存重量
     */
    private BigDecimal weight;

    /**
     * 重量单位
     */
    private String unitWeight;

    /**
     * 创建时间
     */
    private Timestamp createTime;
    private String storeKeeper;
    /**
     * 0无效 1有效
     */
    private Short status;

    private String storeId;

    private BigDecimal capitalCost;
    private String quoteId;
    private BigDecimal undistWeight;
}