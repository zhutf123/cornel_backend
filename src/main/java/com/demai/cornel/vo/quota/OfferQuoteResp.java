package com.demai.cornel.vo.quota;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author binz.zhang
 * @Date: 2019-12-31    13:57
 * 报价返回model
 */

@Data @AllArgsConstructor @NoArgsConstructor @Builder public class OfferQuoteResp {
    private String quoteId;
    private Integer quoteStatus;
    private Integer status;

    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"),
        SERVER_ERROR(1, "网络异常，请稍后重试"), DRY_TOWER_ERROR(2, "烘干塔无效"),
        ORDER_INVALD(3,"订单无效"),USER_ERR(4,"用户无权限"),ORDER_STATUS_INVALD(4,"订单不可修改");

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
