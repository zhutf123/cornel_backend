/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.order;

import java.io.Serializable;

import lombok.Data;

/**
 * Create By tfzhu 2019/12/21 5:04 PM
 */
@Data
public class OperationOrderResp implements Serializable {
    private Boolean success;
    private String orderId;
    private Long orderStatus;
    /***
     * 出货完成时 0 成功 1:司机
     */
    private Long opResult;
    /***
     * 操作完成时间
     */
    private String opOverTime;

    private Long realWeight;

}
