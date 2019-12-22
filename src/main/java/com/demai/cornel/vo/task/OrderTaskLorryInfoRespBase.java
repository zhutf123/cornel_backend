/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.task;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Create By tfzhu  2019/12/22  3:15 PM
 */
@Data
public class OrderTaskLorryInfoRespBase extends  OrderAndTaskRespBase implements Serializable {
    /**
     * 车辆信息id
     */
    private Integer lorryId;
    /**
     * 车牌号
     */
    private String plateNumber;
    /**
     * 车辆载重
     */
    private BigDecimal carryWeight;
    /**
     * 车辆超载重量
     */
    private BigDecimal overCarryWight;

    private String driverName;

    private List<String> driverMobile;

}
