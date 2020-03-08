package com.demai.cornel.demeManager.vo;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminGetQuteDetail extends AdminGetQuoteListResp {
    private String location;
    private Integer optStatus;
    private String createTime;//下单时间
    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"),
        SUCCESS(0, "请求成功"),
        ORDER_INVALID(1, "订单无效"),
        USER_ERROR(2, "用户无权限"),
        SERVER_ERROR(3, "用户无权限");

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
