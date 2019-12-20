package com.demai.cornel.vo.task;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2019-12-19    21:24
 */
@Data
public class GetOrderListReq {
    private String orderId;
    private Integer orderTyp;//获取带收粮  已取消订单    订单状态详情 见wiki：https://yapi.demeteria.com/project/13/wiki
    private Integer pgSize;
}
