/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.order;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.io.Serializable;

/**
 * Create By tfzhu 2019/12/21 5:04 PM
 */
@Data
public class OperationOrderResp implements Serializable {
    private Boolean success;
    private String orderId;
    private Long orderStatus;
}
