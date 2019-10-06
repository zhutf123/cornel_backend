/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.databaseHandler;

/**
 * Create By zhutf  19-10-6  下午4:42
 */
public class HstoreStringCountTypeHandler extends HstoreCountTypeHandler<String> {

    @Override
    protected String toKey(String key) throws IllegalArgumentException {
        return key;
    }

}
