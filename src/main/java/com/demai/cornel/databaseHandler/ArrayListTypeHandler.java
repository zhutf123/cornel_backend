package com.demai.cornel.databaseHandler;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-02-04    12:20
 */
public abstract class ArrayListTypeHandler <T> extends BaseTypeHandler<List<T>> {

    private static final Logger LOG = LoggerFactory.getLogger(ArraySetTypeHandler.class);
    private final String baseType;

    /**
     * @param baseType postgres base type of the postgres array
     */
    public ArrayListTypeHandler(String baseType) {
        this.baseType = baseType;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType)
            throws SQLException {
        Array array = ps.getConnection().createArrayOf(baseType, parameter.toArray());
        ps.setArray(i, array);
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return fromString(rs.getString(columnName));
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return fromString(rs.getString(columnIndex));
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return fromString(cs.getString(columnIndex));
    }

    //TODO string with split
    private List<T> fromString(String array) {
        List<T> set = Lists.newArrayList();
        if (!Strings.isNullOrEmpty(array)) {
            String n = array.substring(1, array.length()-1);
            if (!Strings.isNullOrEmpty(n)) {
                for (String x : n.split(",")) {
                    T val = convert(x);
                    if (val != null) {
                        set.add(val);
                    }
                }
            }
        }
        return set;
    }

    protected abstract T convert(String x);

}
