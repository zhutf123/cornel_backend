/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.databaseHandler;

/**
 * Create By zhutf 19-10-6 下午4:37
 */
public abstract class HstoreCountTypeHandler<KEY extends Comparable> extends HstoreTypeHandler<KEY, Integer> {

    @Override
    protected Integer toValue(String value) throws IllegalArgumentException {
        return Integer.parseInt(value);
    }
}
