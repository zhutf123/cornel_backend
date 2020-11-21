package com.demai.cornel.purcharse.model;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-14    10:58
 */
@Data
public class LocationInfo {
    /**
     * 位置ID
     */
    private Integer id;

    private String locationId;

    /**
     * 位置
     */
    private String location;

    /**
     * 经纬度
     */
    private String locationGis;

    private Integer status;

    private String locationArea;

    private String locationDetail;

}