package com.demai.cornel.demeManager.vo;

import com.demai.cornel.purcharse.vo.resp.GetSaleOrderListResp;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    10:31
 */
@Data public class AdminGetSaleList {
    private String orderId;
    private String buyerId;
    private String contactUserName;
    private String mobile;
    private String receiveLocation;
    private String commodityName;
    private BigDecimal weight;
    private BigDecimal commodityPrice;
    private BigDecimal orderPrice;
    private String receiveStartTime;
    private String receiveEndTime;
    private String unitPrice;
    private String unitWeight;
    private String status;


}
