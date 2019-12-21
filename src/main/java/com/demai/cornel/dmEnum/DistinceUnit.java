package com.demai.cornel.dmEnum;

/**
 * @Author binz.zhang
 * @Date: 2019-12-21    16:13
 */
public enum DistinceUnit {
    KM("KM", "公里"), M("M", "米");
    private String name;
    private String expr;

    DistinceUnit(String name, String expr) {
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
