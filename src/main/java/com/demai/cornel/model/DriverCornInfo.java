package com.demai.cornel.model;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    19:02
 */
@Data
public class DriverCornInfo {
    private String name;
    private String userId;
    private String mobile;
    private String idCard;
    private Integer idType = 1;

}
