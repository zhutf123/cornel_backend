package com.demai.purcharse.model;

import java.math.BigDecimal;
import java.util.Date;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:28
*/
public class WaybillInfo {
    /**
    * 自增ID
    */
    private Integer id;

    /**
    * 承运人ID
    */
    private String carrierId;

    /**
    * 运输方式
    */
    private String transportId;

    /**
    * 起始地址
    */
    private String fromAddressId;

    /**
    * 终止地址
    */
    private String toAddressId;

    /**
    * 开始运输时间
    */
    private Date startTime;

    /**
    * 接货时间
    */
    private Date endTime;

    /**
    * 货物重量
    */
    private BigDecimal weight;

    /**
    * 重量单位
    */
    private String unitWeight;

    /**
    * 运费
    */
    private BigDecimal price;

    /**
    * 价格单位
    */
    private String unitPrice;

    /**
    * 货物ID
    */
    private String cargoId;

    /**
    * 父货物ID
    */
    private String parentCargoId;

    /**
    * 状态 0待运输 1运输中 2 运输完成 3 取消
    */
    private Integer status;

    /**
    * 更新时间
    */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    public String getFromAddressId() {
        return fromAddressId;
    }

    public void setFromAddressId(String fromAddressId) {
        this.fromAddressId = fromAddressId;
    }

    public String getToAddressId() {
        return toAddressId;
    }

    public void setToAddressId(String toAddressId) {
        this.toAddressId = toAddressId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCargoId() {
        return cargoId;
    }

    public void setCargoId(String cargoId) {
        this.cargoId = cargoId;
    }

    public String getParentCargoId() {
        return parentCargoId;
    }

    public void setParentCargoId(String parentCargoId) {
        this.parentCargoId = parentCargoId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}