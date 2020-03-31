package com.demai.cornel.demeManager.vo;

import com.hp.gagawa.java.elements.B;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    18:17
 */
@Data public class AdminGetOutStackInfo {
    private String storeId; //库存ID
    private String fromLocation;// 发货地
    private BigDecimal buyingPrice;//货品买入价格
    private BigDecimal capitalCost = new BigDecimal(0);// 资金使用成本
    private List<OtherInfo> freightAndIncome;// 资金使用成本

    @Data public static class OtherInfo {
        private String freightId = "";//货运ID
        private BigDecimal freightPrice = new BigDecimal(0);//运费
        private String transportType = "";//货运方式
        private BigDecimal inCome = new BigDecimal(0);//收益

    }
}
