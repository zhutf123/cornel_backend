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

        private String plateNumber;
        private String frameNumber;
        private String engineNo;
        private String lorryType;
        private double carryWeight;
        private String carLiceOwner;
        private String carleLiceOwner;
        private List<ImgInfoReq> imgURL;
    }
}