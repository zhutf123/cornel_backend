package com.demai.cornel.purcharse.vo.req;

import com.demai.cornel.model.ImgInfoReq;
import com.demai.cornel.purcharse.model.BuyerCornInfo;
import com.demai.cornel.purcharse.model.BuyerInfo;
import lombok.Data;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-19    23:35
 */
@Data
public class BuyerCplUserInfoReq extends BuyerCornInfo {
    private List<ImgInfoReq> imgs;
}
