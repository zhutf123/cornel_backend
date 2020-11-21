package com.demai.cornel.vo.order;

import lombok.Builder;
import lombok.Data;

import java.lang.ref.PhantomReference;

/**
 * @Author binz.zhang
 * @Date: 2019-12-22    16:55
 */
@Data @Builder public class ArriveArrResp {
    private String orderId;
    /**订单状态***/
    private Long orderStatus;

    /**港口接货码***/
    private String verCode;

    /**操作状态表示*** 0成功 1 请联系烘干塔确认收货 2 请联系烘干塔装车**/
    private Integer opResult;
}
