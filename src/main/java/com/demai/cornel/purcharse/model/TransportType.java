package com.demai.cornel.purcharse.model;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:32
*/
public class TransportType {
    /**
    * 自增ID
    */
    private Integer id;

    /**
    * 运输方式 货运、空运、水运
    */
    private String transportType;

    /**
    * uuid 
    */
    private String transportId;

    /**
    * 0 无效 1有效
    */
    private Short status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }
}