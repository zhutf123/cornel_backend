package com.demai.cornel.demeManager.vo;

import com.demai.cornel.demeManager.model.FreightExInfo;
import com.demai.cornel.purcharse.model.TransportType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    20:56
 */
@Data public class AdminGetFreightResp extends AdminGetFreightViewResp {

    private List<FreightDetailInfo> freightInfo;

    @Data public static class FreightDetailInfo {
        private String freightId;
        private String toLocation;
        private String toLocationId;
        private BigDecimal totalPrice;
        private String transportType;
        private List<FreightExInfo> exInfo;
        private Integer updateFlag=0;
    }
}
