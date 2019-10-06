/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.databaseHandler;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Functions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.HStoreConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Create By zhutf  19-10-6  下午4:37
 */
public abstract class HstoreTypeHandler<KEY extends Comparable, VALUE extends Comparable>
        extends BaseTypeHandler<Map<KEY, VALUE>> {

    public static final Logger LOGGER = LoggerFactory.getLogger(HstoreTypeHandler.class);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<KEY, VALUE> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, HStoreConverter.toString(parameter));
    }

    @Override
    public Map<KEY, VALUE> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return fromString(rs.getString(columnName));
    }

    @Override
    public Map<KEY, VALUE> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return fromString(rs.getString(columnIndex));
    }

    @Override
    public Map<KEY, VALUE> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return fromString(cs.getString(columnIndex));
    }

    private Map<KEY, VALUE> fromString(String hstring) {
        HashMap<KEY, VALUE> typedMap = Maps.newHashMap();
        if (!Strings.isNullOrEmpty(hstring)) {
            Map<String, String> rawMap = HStoreConverter.fromString(hstring);
            for (Map.Entry<String, String> entry : rawMap.entrySet()) {
                try {
                    typedMap.put(toKey(entry.getKey()), toValue(entry.getValue()));
                } catch (Exception e) {
                    LOGGER.warn("hstring:{}, entry key:{}, entry value:{}, convert failed.ex:{}", new Object[] {
                            hstring, entry.getKey(), entry.getValue(), ExceptionUtils.getStackTrace(e)
                    });
                }
            }
        }
        return typedMap;
        //return sortMap(typedMap);
    }

    /**
     * Can be overridden to return sorted maps in custom manners.
     * By default sorts the map according to the count values in descending order.
     */
    protected Map<KEY, VALUE> sortMap(HashMap<KEY, VALUE> map) {
        return sortMapByValuesDesc(map);
    }

    @VisibleForTesting
    protected static <KEY extends Comparable, VALUE extends Comparable> Map<KEY, VALUE> sortMapByValuesDesc(
            HashMap<KEY, VALUE> map) {
        final Ordering<KEY> reverseValuesAndNaturalKeysOrdering =
                Ordering.natural().reverse().nullsLast().onResultOf(Functions.forMap(map, null)) // natural for values
                        .compound(Ordering.natural()); // secondary - natural ordering of keys
        return ImmutableSortedMap.copyOf(map, reverseValuesAndNaturalKeysOrdering);
    }

    /**
     * 转为Key
     *
     * @param key
     * @return
     * @throws IllegalArgumentException
     */
    protected abstract KEY toKey(String key) throws IllegalArgumentException;

    /**
     * 转为VALUE
     *
     * @param value
     * @return
     * @throws IllegalArgumentException
     */
    protected abstract VALUE toValue(String value) throws IllegalArgumentException;
}

