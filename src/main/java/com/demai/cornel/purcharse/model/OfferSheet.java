package com.demai.cornel.purcharse.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    17:06
 */
public class OfferSheet {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 商品ID
     */
    private String commodityId;

    /**
     * 发货地
     */
    private String locationId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 价格单位元/吨
     */
    private String unitPrice;

    /**
     * 0无效 1有效
     */
    private Short status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 报价ID
     */
    private String offerId;

    /**
     * 注意事项
     */
    private String notice;

    private BigDecimal minBuyWeight;

    /**
     * 重量单位
     */
    private String unitWeight;

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

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public BigDecimal getMinBuyWeight() {
        return minBuyWeight;
    }

    public void setMinBuyWeight(BigDecimal minBuyWeight) {
        this.minBuyWeight = minBuyWeight;
    }

    public String getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(String unitWeight) {
        this.unitWeight = unitWeight;
    }
}