package com.demai.cornel.vo.delivery;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-09    22:35
 */
@Data
public class GetSupplierCornInfoResp extends SupplierCplUserInfoReq {
    private Integer optResult;
    public static enum STATUS implements IEmus {
        SUCCESS(0, "成功"), NO_USER(1, "未查到用户信息"),SERVER_ERROR(3, "服务错误");

        private int value;
        private String expr;

        private STATUS(int value, String expr) {
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
