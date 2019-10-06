/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.databaseHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.joda.time.DateTime;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Create By zhutf  19-10-6  下午4:44
 */
@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class JodaDatetimeHandler extends BaseTypeHandler<DateTime> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, DateTime dateTime, JdbcType jdbcType) throws SQLException {
        if (dateTime != null){
            preparedStatement.setObject(i, new Timestamp(dateTime.getMillis()));
        }
    }

    @Override
    public DateTime getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(columnName);
        if (timestamp == null) {
            return null;
        }
        return  new DateTime(timestamp.getTime());
    }

    @Override public DateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public DateTime getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        Timestamp timestamp = callableStatement.getTimestamp(columnIndex);
        if (timestamp == null) {
            return null;
        }
        return new DateTime(timestamp.getTime());
    }
}
