/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.task;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Create By tfzhu  2019/12/22  12:08 PM
 */
@Data
public class TaskInfoRespBase implements Serializable {
    private String orderId;
    private String taskId;
    private String title;
    private String dep;
    private String arr;
    private String depGis;
    private String arrGis;
    private String startValidDate;
    private String endValidDate;
    private String supplierMobile;
    private Set<String> supplierMobileSet;
    private BigDecimal distance;

}
