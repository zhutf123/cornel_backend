package com.demai.cornel.purcharse.model;

import java.math.BigDecimal;
import java.util.Date;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:32
*/
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
    private Date orderTime;

    /**
    * 预计送达时间
    */
    private Date estimateReceiveTime;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCargoId() {
        return cargoId;
    }

    public void setCargoId(String cargoId) {
        this.cargoId = cargoId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getEstimateReceiveTime() {
        return estimateReceiveTime;
    }

    public void setEstimateReceiveTime(Date estimateReceiveTime) {
        this.estimateReceiveTime = estimateReceiveTime;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(BigDecimal freightPrice) {
        this.freightPrice = freightPrice;
    }

    public BigDecimal getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(BigDecimal commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getUintWeight() {
        return uintWeight;
    }

    public void setUintWeight(String uintWeight) {
        this.uintWeight = uintWeight;
    }

    public String getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(String waybillId) {
        this.waybillId = waybillId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }
}