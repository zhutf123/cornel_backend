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
public class BuyOfferResp {
    private String orderId;
    private Integer orderStatus;
    private Integer status;
    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"),
        SUCCESS(0, "请求成功"),
        SERVER_ERROR(1, "网络异常，请稍后重试"),
        USER_ERROR(2, "用户无权限下单"),
        WEIGHT_INVALID(3,"采购重量必须大于最下采购重量");

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
