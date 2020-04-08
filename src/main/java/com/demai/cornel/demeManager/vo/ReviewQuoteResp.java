package com.demai.cornel.demeManager.vo;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    14:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewQuoteResp {
    private Integer optStatus;
    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"),
        SUCCESS(0, "请求成功"),
        ORDER_INVALID(1, "订单无效"),
        USER_ERROR(2, "用户无权限"),
        ORDER_STATAUS_INVALID(3 ,"订单状态不可修改"),
        SERVER_ERR(4 ,"服务异常"),
        NO_LOAN_INFO(5,"没有贷款信息");


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
