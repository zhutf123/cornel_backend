package com.demai.purcharse.model;

import java.math.BigDecimal;
import java.util.Date;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:27
*/
public class PurchaseInfo {
    /**
    * 自增ID
    */
    private Integer id;

    /**
    * 求购商品
    */
    private String commodityId;

    /**
    * 买家ID
    */
    private String userId;

    /**
    * 单价
    */
    private BigDecimal price;

    /**
    * 求购数量
    */
    private BigDecimal weight;

    /**
    * 价格单位
    */
    private String unitPrice;

    /**
    * 重量单位
    */
    private String unitWeight;

    /**
    * 收货地址
    */
    private String addressId;

    /**
    * 收货起始时间
    */
    private Date receiveStart;

    /**
    * 收货截止时间
    */
    private Date receiveEnd;

    /**
    * 0 无效 1有效
    */
    private Short status;

    /**
    * 创建时间
    */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(String unitWeight) {
        this.unitWeight = unitWeight;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public Date getReceiveStart() {
        return receiveStart;
    }

    public void setReceiveStart(Date receiveStart) {
        this.receiveStart = receiveStart;
    }

    public Date getReceiveEnd() {
        return receiveEnd;
    }

    public void setReceiveEnd(Date receiveEnd) {
        this.receiveEnd = receiveEnd;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}