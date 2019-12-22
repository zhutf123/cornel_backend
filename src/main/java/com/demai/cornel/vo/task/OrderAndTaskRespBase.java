/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.task;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Create By tfzhu  2019/12/22  3:15 PM
 */
@Data
public class OrderAndTaskRespBase extends  TaskInfoRespBase {
    private String verifyCode;
    private String serviceMobile;
    private String orderWeight;
    private String startTime;

    private BigDecimal unitWeightPrice;
    private String unitPrice = "元";
    private String unitWeight = "吨";
    private String unitDistance = "km";

}
