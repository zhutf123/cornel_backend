package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author binz.zhang
 * @Date: 2020-02-14    17:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemOfferResp {
    private String orderId;
    private Integer orderStatus;
    private Integer status;
    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"),
        SUCCESS(0, "请求成功"),
        SERVER_ERROR(1, "网络异常，请稍后重试"),
        OFFER_INVALID(2, "报价无效");

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
