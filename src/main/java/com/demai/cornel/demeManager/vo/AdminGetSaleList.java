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
    private String orderId;//订单ID
    private String buyerId;//买家ID
    private String contactUserName;//联系人
    private String mobile;// 手机哈
    private String receiveLocation;//收货地址
    private String commodityName;// 商品名称
    private BigDecimal weight;// 购买重量
    private BigDecimal commodityPrice;//商品单价
    private BigDecimal orderPrice;//订单单价
    private String receiveStartTime;//收货开始时间
    private String receiveEndTime;//收货结束时间
    private String unitPrice;//价格单位
    private String unitWeight;//重量单位
    private String status;
    private String receiveLocationId;//收货地 ID
}
