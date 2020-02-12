package com.demai.cornel.purcharse.model;

import java.math.BigDecimal;
import java.util.Date;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:34
*/
public class CargoId {
    /**
    * 自增ID
    */
    private Integer id;

    /**
    * 重量
    */
    private BigDecimal weight;

    /**
    * 单位
    */
    private String unitWeight;

    /**
    * 货物当前位置
    */
    private String locationId;

    /**
    * 父货物ID
    */
    private String parentCargoId;

    /**
    * 成交时间
    */
    private Date dealTime;

    /**
    * 成交单价
    */
    private BigDecimal price;

    /**
    * 商品ID
    */
    private String commodityId;

    /**
    * 状态
    */
    private Integer status;

    /**
    * 从个库存来的
    */
    private String storeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getParentCargoId() {
        return parentCargoId;
    }

    public void setParentCargoId(String parentCargoId) {
        this.parentCargoId = parentCargoId;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}