package com.test.springboot.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 肖龙威
 * @date 2022/05/11 15:36
 */
@Data
@Setter
@Getter
@ConfigurationProperties(prefix = "hello")
public class Hello {

    public Hello(){

    }

    private String name;

    private Integer age;

    private Integer count;

    private String describe;

    private char[] chars;

}
