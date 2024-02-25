package com.xlw.test.uploader_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.xlw.test.uploader_demo.dao")
@EnableTransactionManagement
@SpringBootApplication
public class UploaderDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploaderDemoApplication.class, args);
    }

}
