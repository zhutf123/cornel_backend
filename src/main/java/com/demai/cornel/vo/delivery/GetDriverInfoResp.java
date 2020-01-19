package com.demai.cornel.vo.delivery;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author binz.zhang
 * @Date: 2020-01-17    20:04
 */
@Data
public class GetDriverInfoResp {
    private String userId;
    private String name;
    private String idCard;
    private List<CarInfo> carInfos;
    @Data
    public static class CarInfo {
        private Long id;
        private String lorryType;
        private BigDecimal carryWeight;
        private String plateNumber;
        private String frameNumber;
        private Integer carLiceOwner;//0 本人 1 其他人 2 公司
    }
}
