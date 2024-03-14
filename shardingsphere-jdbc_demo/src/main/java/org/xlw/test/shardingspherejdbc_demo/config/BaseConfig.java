package org.xlw.test.shardingspherejdbc_demo.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @Title: BaseConfig
 * @Author xlw
 * @Package org.xlw.test.shardingspherejdbc_demo.config
 * @Date 2024/3/14 16:09
 */
@Configuration
public class BaseConfig {

    @Bean
    public SimpleModule simpleModule() {
        SimpleModule simpleModule = new SimpleModule();
        //出参Long类型转String类型
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        return simpleModule;
    }
}
