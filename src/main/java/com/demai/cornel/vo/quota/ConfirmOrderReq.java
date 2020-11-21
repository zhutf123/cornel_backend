package com.demai.cornel.vo.quota;

import lombok.Data;

import javax.naming.ldap.PagedResultsControl;
import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-04-09    10:42
 */
@Data
public class ConfirmOrderReq {
    private BigDecimal shipmentWeight;
    private String  warehouseTime;
    private String quoteId;
    private BigDecimal quote;
    private String showWarehouseTime;
}
