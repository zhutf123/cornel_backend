package com.demai.cornel.demeManager.vo;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.purcharse.model.TransportType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    10:50
 */
/*管理员获取到待审核订单的detail*/
@Data public class AdminUnRevSaleDetail extends AdminUnRevSaleList {
    private Integer optStatus;
    private List<AdminGetOutStackInfo.OtherInfo> freightAndIncome;

    private int showStackInfo = 0;
    @JsonIgnore private String commodityId;

    @JsonIgnore private String outStackId;
    private String storeId; //库存ID
    private String fromLocation;// 发货地
    @JsonIgnore private String fromLocationId;
    private BigDecimal buyingPrice;//货品买入价格
    private BigDecimal capitalCost;// 资金使用成本
    private String freightId;//货运ID
    private BigDecimal freightPrice = new BigDecimal(0);//运费
    private String transportType;//货运方式
    private BigDecimal esInCome;//收益
    private String reviewUser;//收益
    private Set<String> transportTypeMap;
    private Integer shockFlag=0;//0 成功 1 库存不足 2无货运信息
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
    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"), ORDER_INVALID(1, "订单无效"), USER_ERROR(2, "用户无权限"), SERVER_ERROR(3,
                "服务器异常");

        private int value;
        private String expr;

        private STATUS_ENUE(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return value;
        }

        @Override public String getExpr() {
            return expr;
        }
    }
}
