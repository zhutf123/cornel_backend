/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.user;

import com.demai.cornel.vo.supplier.SupplierInfoResp;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Create By tfzhu  2021/1/8  10:25 AM
 *
 * @author tfzhu
 */
@Data public class OtherUserInfoListResp implements Serializable {
    private List<SupplierInfoResp> userInfos;
    private Boolean canEdit;
}
