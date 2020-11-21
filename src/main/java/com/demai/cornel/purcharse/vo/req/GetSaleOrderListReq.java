package com.demai.cornel.purcharse.vo.req;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-18    21:37
 */
@Data public class GetSaleOrderListReq {
    private String orderId;
    private Integer orderType;
    private Integer pgSize;

}
