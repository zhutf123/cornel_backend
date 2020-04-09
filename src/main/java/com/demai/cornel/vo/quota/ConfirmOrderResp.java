package com.demai.cornel.vo.quota;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-04-09    10:46
 */
@Builder
@Data public class ConfirmOrderResp {
    private Integer status;
    private Integer optResult;

    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"), USER_ERROR(1, "用户无权限"), ORDER_ERROR(2, "商品已失效"), ORDER_UNCHANGE(3,
                "订单不可更改"), SERVER_ERR(4, "F服务异常"),ORDER_CHANGE(5,"订单信息发生变化");

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
