package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-22    14:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPurchaseBargainResp {
    private List<priceDetail> priceDetail;
    private Integer optStatus;
    @Data
    public static class priceDetail {
        private BigDecimal beforeOrderPrice;
        private BigDecimal afterOrderPrice;
        private BigDecimal increase;
        private BigDecimal beforeUnitPrice;
        private BigDecimal afterUnitPrice;
    }
    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"),
        SERVER_ERROR(1, "网络异常，请稍后重试"),
        USER_ERROR(2, "用户权限"),
        purcahse_INVALID(3,"订单无效");

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
