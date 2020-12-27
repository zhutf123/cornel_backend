package com.demai.cornel.dmEnum;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    19:12
 */
public enum IdTypeEnum implements IEmus{
    IDCARD(1, "S身份证"), DRIVER_LICE(2, "驾驶证");
    private int value;
    private String expr;

    private IdTypeEnum(int value, String expr) {
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
