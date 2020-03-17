package com.demai.cornel.vo.quota;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-03-17    18:04
 */
@Data
@Builder
public class GetQuotePriceResp {
    private BigDecimal quote;
    private Integer optResult;
    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"), USER_ERROR(1, "用户角色不是烘干塔"),  COMMODITY_ERROR(2, "商品已失效"),
        SERVER_ERR(3,"F服务异常");

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
