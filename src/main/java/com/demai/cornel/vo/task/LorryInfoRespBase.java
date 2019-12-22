/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.task;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Create By tfzhu  2019/12/22  12:09 PM
 */
@Data
public class LorryInfoRespBase implements Serializable {
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

}
