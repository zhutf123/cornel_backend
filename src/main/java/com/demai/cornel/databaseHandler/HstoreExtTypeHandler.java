/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.databaseHandler;

/**
 * Create By zhutf  19-10-6  下午4:38
 */
public class HstoreExtTypeHandler extends HstoreTypeHandler<String, String> {
    @Override protected String toKey(String key) throws IllegalArgumentException {
        return key;
    }

    @Override protected String toValue(String value) throws IllegalArgumentException {
        return value;
    }
}

