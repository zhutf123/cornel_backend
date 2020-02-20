package com.demai.cornel.purcharse.vo.req;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-19    20:15
 */
@Data
public class GetPurchaseListReq {
    private String purchaseId;
    private Integer pgSize;
}
