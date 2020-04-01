package com.demai.cornel.demeManager.vo;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    18:33
 */

@Builder @AllArgsConstructor @Data public class AdminGetSaleDetail extends AdminGetSaleList {

    private Integer optStatus;
    private String storeId; //库存ID
    private String fromLocation;// 发货地
    private BigDecimal buyingPrice;//货品买入价格
    private BigDecimal capitalCost;// 资金使用成本
    private String freightId;//货运ID
    private BigDecimal freightPrice = new BigDecimal(0);//运费
    private String transportType;//货运方式
    private BigDecimal esInCome;//收益
    private String reviewUser;//收益

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

    public AdminGetSaleDetail() {
        this.storeId = "";
        this.fromLocation = "";
        this.buyingPrice = new BigDecimal(0);
        this.capitalCost = new BigDecimal(0);
        this.freightId = "";
        this.transportType = "";
        this.freightPrice = new BigDecimal(0);
        this.esInCome = new BigDecimal(0);
        this.optStatus = STATUS_ENUE.SUCCESS.getValue();
        this.reviewUser="";
    }
}
