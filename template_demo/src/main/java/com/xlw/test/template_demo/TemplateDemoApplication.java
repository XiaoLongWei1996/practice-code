package com.xlw.test.template_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author ABC
 */
@ServletComponentScan(basePackages = "com.xlw.test.template_demo.config")
@EnableScheduling
@EnableRetry
@MapperScan
@EnableSwagger2WebMvc
@EnableTransactionManagement
@SpringBootApplication
public class TemplateDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateDemoApplication.class, args);
    }

}
