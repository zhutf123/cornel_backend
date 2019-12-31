/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.databaseHandler;

import com.fasterxml.jackson.databind.JavaType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * Create By zhutf  19-10-6  下午4:38
 */
@MappedJdbcTypes(JdbcType.OTHER)

public class HstoreExtTypeHandler extends HstoreTypeHandler<String, String> {
    @Override protected String toKey(String key) throws IllegalArgumentException {
        return key;
    }

    @Override protected String toValue(String value) throws IllegalArgumentException {
        return value;
    }
}

