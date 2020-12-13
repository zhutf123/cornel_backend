/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.databaseHandler;

import com.demai.cornel.util.DateFormatUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.joda.time.DateTime;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Create By zhutf  19-10-6  下午4:44
 */
@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class JodaDatetimeHandler extends BaseTypeHandler<String> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String date, JdbcType jdbcType) throws SQLException {
        if (date != null){
            preparedStatement.setObject(i, Timestamp.valueOf(date));
        }
    }


    @Override
    public String getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(columnName);
        if (timestamp == null) {
            return null;
        }
        return  new SimpleDateFormat(DateFormatUtils.ISO_DATETIME_PATTERN).format(timestamp);
    }

    @Override public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public String getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        Timestamp timestamp = callableStatement.getTimestamp(columnIndex);
        if (timestamp == null) {
            return null;
        }
        return new SimpleDateFormat(DateFormatUtils.ISO_DATETIME_PATTERN).format(timestamp);
    }
}
