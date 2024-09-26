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
    //@Bean
    //public ProcessEngine processEngine() {
    //    ProcessEngineConfiguration config = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
    //    //设置数据源，默认使用springboot配置的数据源。(创建流程引擎需要的表，默认创建的)
    //    config
    //            .setJdbcUrl("jdbc:mysql://47.96.12.138:23307/test?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true&useConfigs=maxPerformance&useSSL=false&useTimezone=true&serverTimezone=GMT%2B8")
    //            .setJdbcDriver("com.mysql.cj.jdbc.Driver")
    //            .setJdbcUsername("root")
    //            .setJdbcPassword("4ca31850774c707a58a77163a04905efQW")
    //            .setJdbcPingEnabled(true);
    //    return null;
    //}
}
