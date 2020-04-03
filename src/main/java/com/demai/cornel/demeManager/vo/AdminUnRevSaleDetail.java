package com.demai.cornel.demeManager.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    10:50
 */
/*管理员获取到待审核订单的detail*/
@Data public class AdminUnRevSaleDetail extends AdminUnRevSaleList {
    private List<AdminGetOutStackInfo.OtherInfo> freightAndIncome;
    private int showStackInfo = 0;
    @JsonIgnore private String commodityId;
    @JsonIgnore private String receiveLocationId;

    @JsonIgnore private String outStackId;
    private String storeId; //库存ID
    private String fromLocation;// 发货地
    private BigDecimal buyingPrice;//货品买入价格
    private BigDecimal capitalCost;// 资金使用成本
    private String freightId;//货运ID
    private BigDecimal freightPrice = new BigDecimal(0);//运费
    private String transportType;//货运方式
    private BigDecimal esInCome;//收益
    private String reviewUser;//收益

}
