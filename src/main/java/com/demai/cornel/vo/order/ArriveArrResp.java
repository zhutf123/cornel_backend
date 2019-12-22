package com.demai.cornel.vo.order;

import lombok.Builder;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2019-12-22    16:55
 */
@Data @Builder public class ArriveArrResp {
    private String orderId;
    private Long status;
}
