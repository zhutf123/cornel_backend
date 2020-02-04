package com.demai.cornel.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-02    13:09
 */
@Data public class SystemQuote {
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
     * 价格单位  元
     */
    private String unitPrice;
    /**
     * 重量单位 吨
     */
    private String unitWeight;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 修改时间
     */
    private Timestamp updateTime;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 注意事项
     */
    private List<String> notice;

}