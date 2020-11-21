package com.demai.cornel.dmEnum;

/**
 * @Author binz.zhang
 * @Date: 2019-12-21    16:14
 */
public enum WeightUnit {
    TONS("吨", "吨"), KG("Kg", "公斤");
    private String name;
    private String expr;

    WeightUnit(String name, String expr) {
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
