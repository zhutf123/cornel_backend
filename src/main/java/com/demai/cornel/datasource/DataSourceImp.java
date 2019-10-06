/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.datasource;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * Create By zhutf 19-10-6 下午2:58
 */
public class DataSourceImp implements DataSource {

    public DataSourceImp(String source) {
        super();
        this.source = source;
    }

    private String source;

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getSource() {
        return source;
    }

    public boolean hasDs() {
        return StringUtils.isNotBlank(source);
    }

    @Override
    public String toString() {
        return source;
    }
}
