package com.demai.cornel.model;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2019-12-23    16:07
 */
@Data
public class DriverTaskResp extends TaskInfoReq {
    private Long taskStatus; //task 订单 对应task的状态
}
