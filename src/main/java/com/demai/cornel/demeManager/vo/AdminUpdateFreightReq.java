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
    private List<FreightDetailInfo> destinationList;

    @Data public static class FreightDetailInfo {
        private String toLocation;
        private String toLocationId;
        private List<AdminUpdateFreightReq.TransportList> transportList;
    }

    @Data public static class TransportList {
        private BigDecimal totalPrice;
        private String transportType;
        private String freightId;
        private List<FreightExInfo> exInfo;
        private Integer isUpdate;
    }
}
