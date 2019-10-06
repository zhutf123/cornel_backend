/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.databaseHandler;

import com.demai.cornel.util.BeanMapper;
import com.demai.cornel.util.json.JsonUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.HStoreConverter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Create By zhutf  19-10-6  下午4:25
 * 使用方式：
 * <result column="price_info" jdbcType="VARCHAR"
 * property="priceInfo" javaType="com.demai.vc.order.vo.flight.PriceInfo"
 * typeHandler="GenericHStoreTypeHandler"/>
 */
public class GenericHStoreTypeHandler<E> extends BaseTypeHandler<E> {

    private final Class<E> javaType;

    public GenericHStoreTypeHandler(Class<E> javaType) {
        Preconditions.checkArgument(javaType != null, "javaType 必须设置");
        this.javaType = javaType;
    }

    @Override public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType)
            throws SQLException {
        BeanMapper beanMapper = new BeanMapper(parameter);
        ps.setString(i, HStoreConverter.toString(beanMapper.getMapWithClazzNoneBean()));

    }

    @Override public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return fromString(rs.getString(columnName));
    }

    @Override public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return fromString(rs.getString(columnIndex));
    }

    @Override public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return fromString(cs.getString(columnIndex));
    }

    private E fromString(String hString) {
        if (!Strings.isNullOrEmpty(hString)) {
            Map rawMap = HStoreConverter.fromString(hString);
            return JsonUtil.fromJson(JsonUtil.toJson(rawMap), javaType);
        }
        return null;
    }
}
