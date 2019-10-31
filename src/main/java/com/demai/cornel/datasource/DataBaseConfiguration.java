package com.demai.cornel.datasource;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import javax.sql.DataSource;

import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;

@Configuration
@MapperScan({"com.demai.cornel.dao","com.demai.cornel.auth.dao"})
public class DataBaseConfiguration {

    public static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    @Primary
    @ConfigurationProperties("datasources.dm-master")
    public HikariConfig dmMasterHikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.addDataSourceProperty("ApplicationName", "");
        hikariConfig.setPoolName("HikariPool-dmMaster");
        return hikariConfig;
    }

    @Bean(value = "ds.master")
    public DataSource dmMasterDataSource() {
        HikariConfig masterConfig = dmMasterHikariConfig();
        DataSource ds = new HikariDataSource(masterConfig);
        LOGGER.info("初始化datasource dm-master成功!{}", ds);
        return ds;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = Maps.newHashMap();
        targetDataSources.put("master", dmMasterDataSource());
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(dmMasterDataSource());
        LOGGER.info("初始化datasource成功!");
        return dynamicDataSource;
    }

    @Bean("transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dmMasterDataSource());
        return transactionManager;
    }

}
