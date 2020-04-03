package com.demai.cornel.demeManager.vo;

import com.demai.cornel.purcharse.model.TransportType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    10:39
 */
/*管理员获取已同意订单的list*/

@Data public class AdminAppSaleList extends AdminGetSaleList {
    private String fromLocation;// 发货地
    private String fromLocationId;// 发货地

    private String transportType;//货运方式
    private BigDecimal esInCome;//收益
    private String reviewUser;//收益

    private Set<String> transportTypeMap;

    public void setTransportTypeMap(Set<String> transportTypeMap) {
        this.transportTypeMap = transportTypeMap;
        if (transportTypeMap != null) {
            StringBuilder stringBuilder = new StringBuilder();
            transportTypeMap.stream().forEach(x -> {
                TransportType.TRANSPORT_TYPE_ENUM stp = TransportType.typeOf(x);
                if (stp != null) {
                    stringBuilder.append(stp.getExpr()).append("+");
                }
            });
            if (stringBuilder.toString().endsWith("+")) {
                transportType = stringBuilder.substring(0, stringBuilder.lastIndexOf("+")).toString();
            }
        }
    }
}
