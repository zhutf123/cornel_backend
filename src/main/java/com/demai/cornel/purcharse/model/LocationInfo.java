package com.demai.cornel.purcharse.model;

import lombok.Data;

/**
* @Author binz.zhang
* @Date: 2020-02-12    15:22
*/
@Data
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


}