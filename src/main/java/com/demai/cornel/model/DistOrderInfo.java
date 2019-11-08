package com.demai.cornel.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author binz.zhang
 * @Date: 2019-11-07    16:50
 */
@Data
public class DistOrderInfo implements Serializable {
    private static final long serialVersionUID = 2326190091185718697L;
    private Long id;
    private String taskId;
    private String distId;
    private String userId;
    private Integer jobNo;
    private Integer jobStatus;
    private Date operationTime;
    private Date createTime;
    private Date expireTime;
    private String orderId;
    private String mobile;
}