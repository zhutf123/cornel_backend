package com.demai.cornel.vo.delivery;

import com.demai.cornel.model.ImgInfoReq;
import lombok.Data;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    14:51
 */
@Data public class DriverCpllCarReq {

    private List<CarInfoBean> carInfo;

    @Data public static class CarInfoBean {
        /**
         * plateNumber : 京A88888
         * frameNumber : 12312nsagds7
         * engineNo : SADSADAS
         * lorryType : 小卡车
         * carryWeight : 890.0
         * carLiceOwner : 本人
         * imgURL : [{"carLice":""},{"driverLice":""},{"carImg":""}]
         * carleLiceOwner : 他人
         */

        private String plateNumber;//车牌号
        private String frameNumber;//车架号
        private String engineNo;//发动机号
        private String lorryType;//货车类型
        private double carryWeight;//载重
        private String carLiceOwner;//行驶证
        private List<ImgInfoReq> imgURL;
    }
}