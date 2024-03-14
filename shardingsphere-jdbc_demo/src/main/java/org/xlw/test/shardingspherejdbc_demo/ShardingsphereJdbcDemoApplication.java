package org.xlw.test.shardingspherejdbc_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan(value = {"org.xlw.test.shardingspherejdbc_demo.dao"})
@SpringBootApplication
public class ShardingsphereJdbcDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingsphereJdbcDemoApplication.class, args);
    }

}
