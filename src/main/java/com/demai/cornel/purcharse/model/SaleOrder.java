package com.demai.cornel.purcharse.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:32
*/
@Data
public class SaleOrder {
    /**
    * 自增ID
    */
    private Integer id;

    /**
    * 订单状态
    */
    private Integer status;

    /**
    * 货品ID
    */
    private String cargoId;

    /**
    * 买家ID
    */
    private String buyerId;

    /**
    * 下单时间
    */
    private Timestamp orderTime;

    /**
    * 预计送达时间
    */
    private Timestamp estimateReceiveTime;

    /**
    * 付款方式
    */
    private String paymentType;

    /**
    * 付款状态 0 待支付 1支付完成 
    */
    private Integer paymentStatus;

    /**
    * 运费
    */
    private BigDecimal freightPrice;

    /**
    * 商品价格
    */
    private BigDecimal commodityPrice;

    /**
    * 订单的总金额=freigh_price+commodity_price
    */
    private BigDecimal orderPrice;

    /**
    * 价格单位
    */
    private String unitPrice;

    /**
    * 重量
    */
    private BigDecimal weight;

    /**
    * 重量单位
    */
    private String uintWeight;

    /**
    * 运单列表
    */
    private String waybillId;

    /**
    * 对应的求购信息 为空则说明是购买的系统挂出的订单
    */
    private String purchaseId;

}