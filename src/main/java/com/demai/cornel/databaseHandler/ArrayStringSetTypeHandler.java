/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.databaseHandler;

/**
 * Create By zhutf  19-10-6  下午4:22
 */
public class ArrayStringSetTypeHandler extends ArraySetTypeHandler<String> {

    public ArrayStringSetTypeHandler() {
        super("varchar");
    }

    @Override protected String convert(String x) {
        return x;
    }
}