package com.demai.cornel.purcharse.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import lombok.Data;

/**
* @Author binz.zhang
* @Date: 2020-03-31    13:09
*/
@Data
public class FreightInfo {
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
    private Date createTime;

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
}