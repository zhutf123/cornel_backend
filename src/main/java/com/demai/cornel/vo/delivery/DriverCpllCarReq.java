package com.demai.cornel.vo.delivery;

import com.demai.cornel.model.CarCornInfo;
import com.demai.cornel.model.ImgInfoReq;
import lombok.Data;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    14:51
 */
@Data public class DriverCpllCarReq extends CarCornInfo {

    private List<ImgInfoReq> imgURL;

}