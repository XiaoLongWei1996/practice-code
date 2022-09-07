package com.springcloud.test.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author 肖龙威
 * @date 2022/09/06 11:15
 */
@Configuration
public class SwaggerConfig {

    @Bean
    Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                //配置网站的基本信息
                .groupName("1")
                .apiInfo(new ApiInfoBuilder()
                        //网站标题
                        .title("cloud测试接口文档")
                        //标题后面的版本号
                        .version("v1.0")
                        .description("cloud测试接口文档")
                        //联系人信息
                        .contact(new Contact("xlw", "", "xlw@qq.com"))
                        .build())
                .select()
                //指定接口的位置
                .apis(RequestHandlerSelectors.basePackage("com.springcloud.test.system.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }


}
