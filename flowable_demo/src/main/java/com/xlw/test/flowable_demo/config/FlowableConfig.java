package com.xlw.test.flowable_demo.config;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 流程引擎配置
 * @Title: FlowableConfig
 * @Author xlw
 * @Package com.xlw.test.flowable_demo.config
 * @Date 2024/9/26 11:10
 */
@RequiredArgsConstructor
@Configuration
public class FlowableConfig {

    private final DataSourceProperties dataSourceProperties;

    /**
     * 流程引擎配置
     *
     * @return {@link ProcessEngine }
     */
    @Bean
    public ProcessEngine processEngine() {
        ProcessEngineConfiguration config = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        //设置数据源，默认使用springboot配置的数据源。(创建流程引擎需要的表，默认创建的)
        config
                .setJdbcUrl(dataSourceProperties.getUrl())
                .setJdbcDriver(dataSourceProperties.getDriverClassName())
                .setJdbcUsername(dataSourceProperties.getUsername())
                .setJdbcPassword(dataSourceProperties.getPassword())
                .setJdbcPingEnabled(true)
                // 初始化基础表，不需要的可以改为 DB_SCHEMA_UPDATE_FALSE
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
                //设置字体
                .setActivityFontName("宋体")
                .setLabelFontName("宋体")
                .setAnnotationFontName("宋体");
        return config.buildProcessEngine();
    }
}
