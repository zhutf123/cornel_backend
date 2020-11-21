package com.demai.cornel.dmEnum;

/**
 * @Author binz.zhang
 * @Date: 2019-12-21    16:09
 */
public enum PriceUnit {
    RMB("元", "人民币"), DOLLAR("美元", "美元");
    private String name;
    private String expr;

    PriceUnit(String name, String expr) {
        this.name = name;
        this.expr = expr;
    }

    public String getValue() {
        return this.name;
    }

     public String getExpr() {
        return null;
    }
}
