package com.demai.cornel.purcharse.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-14    19:58
 */
@Data public class CargoInfo {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 单位
     */
    private String unitWeight;

    /**
     * 货物当前位置
     */
    private String locationId;

    /**
     * 父货物ID
     */
    private String parentCargoId;

    /**
     * 成交时间
     */
    private Timestamp dealTime;

    /**
     * 成交单价
     */
    private BigDecimal price;

    /**
     * 商品ID
     */
    private String commodityId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 从个库存来的
     */
    private String storeId;

    private String cargoId;
}