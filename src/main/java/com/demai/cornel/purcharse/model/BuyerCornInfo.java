package com.demai.cornel.purcharse.model;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-19    23:40
 */
@Data
public class BuyerCornInfo {
    private String userName;
    private Integer idType=1;
    private String idCard;
    private String mobile;
}
