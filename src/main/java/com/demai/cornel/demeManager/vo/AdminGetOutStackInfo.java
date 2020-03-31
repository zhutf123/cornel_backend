package com.demai.cornel.demeManager.vo;

import com.hp.gagawa.java.elements.B;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    18:17
 */
@Data
public class AdminGetOutStackInfo {
    private String storeId; //库存ID
    private String fromLocation;// 发货地
    private String buyingPrice;//货品买入价格
    private BigDecimal capitalCost=new BigDecimal(0);// 资金使用成本
    private BigDecimal freightPrice = new BigDecimal(0);// 货运成本
}
