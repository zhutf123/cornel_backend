package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-17    14:38
 */
@Builder @AllArgsConstructor @NoArgsConstructor @Data public class ClickMyOfferResp {
    private String receiveLocation; //收货地址
    private Integer status;

    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"), USER_ERROR(1, "无权限"), COMMODITY_ERROR(3, "商品已失效");

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
