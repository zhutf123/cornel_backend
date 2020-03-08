package com.demai.cornel.demeManager.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
* @Author binz.zhang
* @Date: 2020-03-08    19:37
*/
@Data
public class SpecialQuote {
    /**
    *  自增ID
    */
    private Integer id;

    /**
    * 报价人
    */
    private String quoteUserId;

    /**
    * 报价ID
    */
    private String quoteId;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 状态0 无效 1有效
    */
    private Integer status;

    private String targetUserId;

    /**
    * 烘干塔ID
    */
    private String targetTowerId;

    /**
    * 商品ID
    */
    private String commodityId;

    /**
    * 价格单位
    */
    private String unitPrice;

    /**
    * 重量单位
    */
    private String unitWeight;

    /**
    * 最小出货量
    */
    private BigDecimal minShipmentWeight;

    /**
    * 价格
    */
    private BigDecimal quote;
}