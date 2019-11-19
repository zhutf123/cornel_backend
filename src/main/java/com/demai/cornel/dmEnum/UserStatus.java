package com.demai.cornel.dmEnum;

/**
 * @Author binz.zhang
 * @Date: 2019-11-07    14:38
 */
public enum UserStatus implements IEmus {
    INVALID(2, "用户无效"), AVAILABLE(1, "有效用户"), BUSY(3, "忙碌中");
    private int value;
    private String expr;

    private UserStatus(int value, String expr) {
        this.value = value;
        this.expr = expr;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getExpr() {
        return expr;
    }
}
