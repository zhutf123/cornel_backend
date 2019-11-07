package com.demai.cornel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2019-11-07    15:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDistOrderModel {
    private String userId;
    private String tel;
    private String userName;
    private String mobile;
    private String idType;
    private String idCade;
    private String lorryType;
    private String plateNumber;
    private BigDecimal carryWeight;
    private BigDecimal distWeigth;
}
