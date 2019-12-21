package com.demai.cornel.vo.order;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author binz.zhang
 * @Date: 2019-12-20 20:54 司机侧获取订单info
 */
@Data
public class GetOrderInfoReq implements Serializable {

    private String verifyCode;
    private String orderId;

}
