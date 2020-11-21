package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Builder;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-24    23:34
 */
@Data
@Builder
public class OptResultResp {

    private Integer optStatus;
    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"),
        SUCCESS(0, "请求成功"),
        SERVER_ERROR(1, "网络异常，请稍后重试"),
        TARGET_ERROR(2, "操作对象已经失效"),
        USER_ERROR(3, "用户无权限");

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
