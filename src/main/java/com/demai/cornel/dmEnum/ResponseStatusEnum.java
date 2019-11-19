/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.dmEnum;

/**
 * Create By zhutf 19-11-1 下午3:25
 */
public enum ResponseStatusEnum implements IEmus {

    NO_USER(-1, "用户不存在，请先注册"), SUCCESS(0, "请求成功"), NETWORK_ERROR(1, "网络异常，请稍后重试");

    private int value;
    private String expr;

    private ResponseStatusEnum(int value, String expr) {
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
