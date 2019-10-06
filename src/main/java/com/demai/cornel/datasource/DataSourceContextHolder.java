/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.datasource;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * Create By zhutf  19-10-6  下午2:28
 */
public class DataSourceContextHolder {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceContextHolder.class);

    private static final ThreadLocal<LinkedList<String>> contextHolder = new ThreadLocal<LinkedList<String>>() {
        @Override
        protected LinkedList<String> initialValue() {
            return Lists.newLinkedList();
        }
    };

    static LinkedList<String> getAllSource() {
        return contextHolder.get();
    }

    static void setDataSourceType(DataSource source) {
        if (StringUtils.isNotBlank(source.getSource())) {
            logger.debug("set data source :{}", source);
            contextHolder.get().push(source.getSource());
        } else {
            logger.error("please set the correct DataSource ,source={}", source.getSource());
        }
    }

    static void setDataSourceType(DataSource source, int num) {
        if (StringUtils.isNotBlank(source.getSource())) {
            logger.debug("set data source :{},num:{}", source, num);
            contextHolder.get().add(num, source.getSource());
        } else {
            logger.error("please set the correct DataSource ,source={},num={}", source.getSource(), num);
        }
    }

    static String getNowSourceType() {
        if (hasDataSourceNow()) {
            String source = contextHolder.get().getFirst();
            logger.debug("execute by data source:{}", source);
            return source;
        } else {
            logger.debug("execute by defult data source");
            return null;
        }
    }

    static String clearDataSourceType() {
        String dataSource = null;
        if (hasDataSourceNow()) {
            dataSource = contextHolder.get().pop();
            logger.debug("method execute finish  , data source clean success ,ds={}", dataSource);
        } else {
            logger.debug("now data source is empty");
        }
        return dataSource;
    }

    static String clearDataSourceType(int num) {
        String dataSource = null;
        if (hasDataSourceNow() && getDSNum() > num) {
            dataSource = contextHolder.get().remove(num);
            logger.debug("The {} data source clean success ,ds={}", num, dataSource);
        } else {
            logger.debug(" location :{} data source is not exist");
        }
        return dataSource;
    }

    static void clearAllDataSourceType() {
        contextHolder.get().clear();
        logger.debug("All data source clean success ");
    }

    static boolean hasDataSourceNow() {
        return CollectionUtils.isNotEmpty(contextHolder.get());
    }

    static int getDSNum() {
        if (CollectionUtils.isEmpty(contextHolder.get())) {
            return 0;
        } else {
            return contextHolder.get().size();
        }
    }
}
