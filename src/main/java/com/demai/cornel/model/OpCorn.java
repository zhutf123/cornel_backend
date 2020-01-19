package com.demai.cornel.model;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Builder;
import lombok.Data;
import sun.dc.pr.PRError;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    20:33
 */
@Data
@Builder
public class OpCorn {
    private Integer optResult;
    public static enum STATUS implements IEmus {
        SUCCESS(0, "成功"), NO_OPT_TARGET(1, "未查到需要更新的对象"),PARAM_ERROR(2, "参数错误"),SERVER_ERROR(3, "服务错误");

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
