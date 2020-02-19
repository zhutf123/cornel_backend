package com.demai.cornel.purcharse.vo.req;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-02-14    11:16
 */
@Data
public class SystemOfferReq {
    private String buyerId;  //下单用户ID
    @NonNull
    private String contactUserName; //联系人
    @NonNull
    private String mobile; //联系电话
    @NonNull
    private String commodityId; //商品ID
    @NonNull
    private String receiveLocationId; //收货地址
    @NonNull
    private BigDecimal weight; //重量
    private String unitWeight = "吨"; //重量单位
    private String offerId; // 首页售卖ID
    @NonNull
    private BigDecimal price;
    private String unitPrice = "元";
    @NonNull
    private String receiveStartTime;//预计收货时间
    private String receiveEndTime;//预计收货时间截止时间

}
