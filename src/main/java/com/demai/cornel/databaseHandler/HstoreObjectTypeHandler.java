/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.databaseHandler;

import com.demai.cornel.model.HstoreType;
import com.demai.cornel.util.BeanMapper;
import com.demai.cornel.util.json.JsonUtil;
import com.google.common.base.Strings;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.HStoreConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Create By zhutf  19-10-6  下午4:39
 */
public class HstoreObjectTypeHandler extends BaseTypeHandler<HstoreType> {

    public static final Logger LOGGER = LoggerFactory.getLogger(HstoreObjectTypeHandler.class);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, HstoreType parameter, JdbcType jdbcType)
            throws SQLException {
        BeanMapper beanMapper = new BeanMapper(parameter);
        ps.setString(i, HStoreConverter.toString(beanMapper.getMapWithClazzNoneBean()));
    }

    @Override
    public HstoreType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return fromString(rs.getString(columnName));
    }

    @Override
    public HstoreType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return fromString(rs.getString(columnIndex));
    }

    @Override
    public HstoreType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return fromString(cs.getString(columnIndex));
    }

    private HstoreType fromString(String hstring) throws SQLException {
        if (!Strings.isNullOrEmpty(hstring)) {
            Map<String, String> rawMap = HStoreConverter.fromString(hstring);
            String clazzName = rawMap.get(BeanMapper.CLAZZ_KEY_NAME);
            if (Strings.isNullOrEmpty(clazzName)) {
                throw new SQLException("@clazz 未查找到!");
            }

            try {
                Class<?> clazz = Class.forName(clazzName);
                if (HstoreType.class.isAssignableFrom(clazz)) {
                    return JsonUtil.fromJson(JsonUtil.toJson(rawMap), (Class<HstoreType>) clazz);
                }
                throw new SQLException("@clazz:" + clazzName + "未继承com.demai.cornel.model.HstoreType !");
            } catch (ClassNotFoundException e) {
                throw new SQLException("@clazz:" + clazzName + "类不存在!", e);
            }
        }
        return null;
    }
}

