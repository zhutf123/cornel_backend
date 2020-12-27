package com.demai.cornel.vo.delivery;

import com.demai.cornel.model.ImgInfoReq;
import com.demai.cornel.model.SupplierCornInfo;
import lombok.Data;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-07    17:45
 */
@Data
public class SupplierOtherUserInfoReq extends SupplierCornInfo {
    private List<ImgInfoReq> imgs;
    private String towerId;

}
