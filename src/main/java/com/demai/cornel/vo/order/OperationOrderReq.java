/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.order;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * Create By tfzhu 2019/12/21 5:04 PM
 */
@Data
public class OperationOrderReq implements Serializable {
    private String orderId;
    private BigDecimal realWeight;
}
