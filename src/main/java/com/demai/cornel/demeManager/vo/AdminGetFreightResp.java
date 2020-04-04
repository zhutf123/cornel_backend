package com.demai.cornel.demeManager.vo;

import com.demai.cornel.demeManager.model.FreightExInfo;
import com.demai.cornel.purcharse.model.TransportType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    20:56
 */
@Data public class AdminGetFreightResp extends AdminGetFreightViewResp {

    private Collection<FreightDetailInfo> destinationList;

    @Data public static class FreightDetailInfo {
        private String toLocation;
        private String toLocationId;
        private List<TransportList> transportList;
    }

    @Data public static class TransportList {
        private BigDecimal totalPrice;
        private String transportType;
        private String freightId;
        private List<FreightExInfo> exInfo;
        private String updateTime;
        private Integer updateFlag = 0;

    }
}
