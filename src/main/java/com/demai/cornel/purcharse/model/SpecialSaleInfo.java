package com.demai.cornel.purcharse.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-04-02    11:42
 */
@Data public class SpecialSaleInfo {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 商品ID
     */
    private String commodityId;

    /**
     * 地理位置
     */
    private String locationId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 单位
     */
    private String unitPrice;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 时间
     */
    private Timestamp createTime;

    private List<String> notice;

    /**
     * 最小购买重量
     */
    private BigDecimal minBuyWeight;

    private String unitWeight;

    private String targetUserId;

    /**
     * 审核人
     */
    private String reviewUser;
}