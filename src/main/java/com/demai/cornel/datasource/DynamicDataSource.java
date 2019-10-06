/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;

import java.util.Map;

/**
 * Create By zhutf 19-10-6 下午2:16
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getNowSourceType();
    }

    @Override
    public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {
        super.setDataSourceLookup(dataSourceLookup);
    }

    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
    }
}
