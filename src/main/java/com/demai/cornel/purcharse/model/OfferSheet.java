package com.demai.cornel.purcharse.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    17:06
 */
@Data
public class OfferSheet {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 商品ID
     */
    private String commodityId;

    /**
     * 发货地
     */
    private String locationId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 价格单位元/吨
     */
    private String unitPrice;

    /**
     * 0无效 1有效
     */
    private short status;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 报价ID
     */
    private String offerId;

    /**
     * 注意事项
     */
    private List<String> notice;

    private BigDecimal minBuyWeight;

    /**
     * 重量单位
     */
    private String unitWeight;

   private String targetUserId;
}