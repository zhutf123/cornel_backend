/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * Create By tfzhu  2020/12/27  11:51 AM
 *
 * @author tfzhu
 */
@Data
public class CompanyParam implements Serializable {

    private String userId;
    private String companyName;
    private String licenseUrl;
    private String companyId;
}
