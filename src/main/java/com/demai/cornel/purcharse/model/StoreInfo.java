package com.demai.cornel.purcharse.model;

import java.math.BigDecimal;
import java.util.Date;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:08
*/
public class StoreInfo {
    /**
    * 自增ID
    */
    private Integer id;

    /**
    * 库存ID
    */
    private String commodityId;

    /**
    * 位置ID
    */
    private String locationId;

    /**
    * 购买价格
    */
    private BigDecimal buyingPrice;

    /**
    * 价格单位
    */
    private String unitPrice;

    /**
    * 库存重量
    */
    private BigDecimal weight;

    /**
    * 重量单位
    */
    private String unitWeight;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 0无效 1有效
    */
    private Short status;

    private String storeId;

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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public BigDecimal getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
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

    public String getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(String unitWeight) {
        this.unitWeight = unitWeight;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}