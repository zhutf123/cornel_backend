package com.demai.cornel.vo.delivery;

import com.demai.cornel.dmEnum.IEmus;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    16:11
 */
public class DriverCpllCarResp {
    private String userId;
    private Integer optResult;
    private String name;

    public static enum STATUS implements IEmus {
        SUCCESS(0, "成功"), PARAM_ERROR(1, "参数缺失"), SERVICE_ERROR(2, "服务异常");

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
