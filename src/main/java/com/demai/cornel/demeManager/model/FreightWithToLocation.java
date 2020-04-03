package com.demai.cornel.demeManager.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    21:23
 */
@Data
public class FreightWithToLocation {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 运费ID
     */
    private String freightId;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 起始地
     */
    private String fromLocation;

    /**
     * 收货地
     */
    private String toLocation;

    /**
     * 货运方式
     */
    private Set<String> transportType;

    /**
     * 运费
     */
    private BigDecimal price;

    /**
     * 耗时单位小时
     */
    private Integer costTime;

    /**
     * 0 无效 1有效
     */
    private Integer status;

    private String exInfo;

    private String toLocationTx;

    private String updateTime;
}
