package com.demai.cornel.purcharse.model;

/**
* @Author binz.zhang
* @Date: 2020-02-12    15:22
*/
public class LocationInfo {
    /**
    * 位置ID
    */
    private Integer id;

    private Integer locationId;

    /**
    * 位置
    */
    private String location;

    /**
    * 经纬度
    */
    private String locationGis;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationGis() {
        return locationGis;
    }

    public void setLocationGis(String locationGis) {
        this.locationGis = locationGis;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}