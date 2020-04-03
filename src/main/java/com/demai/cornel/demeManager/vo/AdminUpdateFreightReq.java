package com.demai.cornel.demeManager.vo;

import com.demai.cornel.demeManager.model.FreightExInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    22:08
 */
@Data public class AdminUpdateFreightReq {

    private String towerId;
    private String fromLocationId;
    private List<FreightUpdatelInfo> freightInfo;

    @Data public static class FreightUpdatelInfo {
        private String freightId;
        private String toLocation;
        private String toLocationId;
        private BigDecimal totalPrice;
        private String transportType;
        private List<FreightExInfo> exInfo;
        private Integer updateFlag;
    }
}
