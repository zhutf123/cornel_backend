package com.demai.cornel.demeManager.vo;

import com.demai.cornel.demeManager.model.ShippProcess;
import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    13:04
 */
@Data @Builder @AllArgsConstructor @NoArgsConstructor public class AdminAppSaleDetail extends AdminAppSaleList {
    private BigDecimal buyingPrice;
    private BigDecimal freightPrice;
    private BigDecimal capitalCost;// 资金使用成本
    private Integer optStatus;
    private String shippProcessKey;
    private Integer shippProcess;//出库顺序

    public void setShippProcess(Integer shippProcess) {
        this.shippProcess = shippProcess;
        ShippProcess.TYPE type = ShippProcess.TYPE.keyOf(shippProcess);
        this.shippProcessKey = type == null ? "" : type.getExpr();
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
