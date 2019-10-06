/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.databaseHandler;

import com.demai.cornel.util.StringUtil;
import com.demai.cornel.util.json.JsonUtil;
import com.google.common.base.Preconditions;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Create By zhutf  19-10-6  下午4:36
 */
public class GenericJsonTypeHandler<E> extends BaseTypeHandler<E> {

    private final Class<E> javaType;

    public GenericJsonTypeHandler(Class<E> javaType) {
        Preconditions.checkArgument(javaType != null, "javaType 必须设置");
        this.javaType = javaType;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtil.toJson(parameter));
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return fromString(rs.getString(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return fromString(rs.getString(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return fromString(cs.getString(columnIndex));
    }

    private E fromString(String jsonString) {
        if (StringUtil.isBlank(jsonString)) {
            return null;
        }
        return JsonUtil.fromJson(jsonString, javaType);
    }
}

