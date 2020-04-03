package com.demai.cornel.demeManager.vo;

import com.demai.cornel.purcharse.vo.resp.GetSaleOrderListResp;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String receiveLocationId;

//    private int showStackInfo=0;
//    @JsonIgnore private String commodityId;
//    @JsonIgnore private String receiveLocationId;
//
//    @JsonIgnore private String outStackId;
//    private String storeId; //库存ID
//    private String fromLocation;// 发货地
//    private BigDecimal buyingPrice;//货品买入价格
//    private BigDecimal capitalCost;// 资金使用成本
//    private String freightId;//货运ID
//    private BigDecimal freightPrice = new BigDecimal(0);//运费
//    private String transportType;//货运方式
//    private BigDecimal esInCome;//收益
//    private String reviewUser;//收益
//
//    private String payReview;
//    private String payTime;
//    private BigDecimal actualPay;
//    private String exInfo;

}
