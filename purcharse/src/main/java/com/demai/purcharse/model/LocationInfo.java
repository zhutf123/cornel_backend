package com.demai.purcharse.model;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:24
*/
public class LocationInfo {
    /**
    * 位置ID
    */
    private Integer id;

    private Integer addressId;

    /**
    * 位置
    */
    private String addressDetai;

    /**
    * 经纬度
    */
    private String addressGis;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getAddressDetai() {
        return addressDetai;
    }

    public void setAddressDetai(String addressDetai) {
        this.addressDetai = addressDetai;
    }

    public String getAddressGis() {
        return addressGis;
    }

    public void setAddressGis(String addressGis) {
        this.addressGis = addressGis;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}