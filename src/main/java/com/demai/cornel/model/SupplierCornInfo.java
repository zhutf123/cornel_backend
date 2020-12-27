package com.demai.cornel.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author binz.zhang
 * @Date: 2020-02-07    17:44
 */
@Data
public class SupplierCornInfo implements Serializable {
    private String name;
    private String userId;
    private String mobile;
    private String idCard;
    private Integer idType = 1;

}
