package com.demai.cornel.demeManager.vo;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Builder;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    16:40
 */
@Builder
public class AdminReviewSaleResp {
    private Integer optStatus;

    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"), ORDER_INVALID(1, "订单无效"),
        USER_ERROR(2, "用户无权限"), STAORE_ERR(3, "库存不足"),ORDER_UN_CHANGE(4,"订单状态不可修改"),SERVER_ERR(5,"服务器异常");

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
