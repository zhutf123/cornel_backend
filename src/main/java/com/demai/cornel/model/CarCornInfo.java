package com.demai.cornel.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    19:03
 */
@Data
public class CarCornInfo {
    private String plateNumber;//车牌号
    private String frameNumber;//车架号
    private String engineNo;//发动机号
    private String lorryType;//货车类型
    private BigDecimal carryWeight;//载重
    private String carLiceOwner;//行驶证
    private String lorryId;//货车类型

}
