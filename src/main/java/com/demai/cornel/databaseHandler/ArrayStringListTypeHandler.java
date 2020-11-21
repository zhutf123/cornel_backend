package com.demai.cornel.databaseHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-02-04    12:18
 */
public class ArrayStringListTypeHandler extends ArrayListTypeHandler<String> {

    /**
     * @param baseType postgres base type of the postgres array
     */
    public ArrayStringListTypeHandler() {
            super("varchar");
    }

    @Override protected String convert(String x) {
        if (x.startsWith("\"") && x.endsWith("\"")){
            x = x.substring(1, x.length()-1);
        }
        return x;
    }
}
