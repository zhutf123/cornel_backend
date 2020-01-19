package com.demai.cornel.vo.delivery;

import com.demai.cornel.model.ImgInfoReq;
import lombok.Data;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    14:51
 */
@Data public class DriverCpllCarReq {

    private String plateNumber;//车牌号
    private String frameNumber;//车架号
    private String engineNo;//发动机号
    private String lorryType;//货车类型
    private double carryWeight;//载重
    private String carLiceOwner;//行驶证
    private List<ImgInfoReq> imgURL;

}