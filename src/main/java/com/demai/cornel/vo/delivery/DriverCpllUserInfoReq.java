package com.demai.cornel.vo.delivery;

import com.demai.cornel.model.DriverCornInfo;
import com.demai.cornel.model.ImgInfoReq;
import lombok.Data;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-17    11:29
 * 司机完善个人信息的
 */
@Data public class DriverCpllUserInfoReq extends DriverCornInfo {

    private List<ImgInfoReq> imgs;

}
