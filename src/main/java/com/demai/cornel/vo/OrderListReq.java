/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * Create By tfzhu 2019/12/19 11:56 AM
 */
@Data
public class OrderListReq implements Serializable {
    /**
     * 请求状态 按照 ","分割
     */
    private Long status;
    

}
