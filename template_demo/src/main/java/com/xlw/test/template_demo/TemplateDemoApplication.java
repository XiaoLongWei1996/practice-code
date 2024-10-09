package com.xlw.test.template_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@MapperScan
@EnableSwagger2WebMvc
@EnableTransactionManagement
@SpringBootApplication
public class TemplateDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateDemoApplication.class, args);
    }

}
