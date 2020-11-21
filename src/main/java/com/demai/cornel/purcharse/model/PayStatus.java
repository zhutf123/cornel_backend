package com.demai.cornel.purcharse.model;

import com.demai.cornel.dmEnum.IEmus;

/**
 * @Author binz.zhang
 * @Date: 2020-02-14    19:31
 */
public enum PayStatus implements IEmus {
    UNPAID(1, "待支付"),
    FINISH(2, "支付完成"),
    CANCEL(0, "已取消");

    private int value;
    private String expr;

    private PayStatus(int value, String expr) {
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
