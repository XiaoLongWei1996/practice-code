package testspringboot.testspringboot.domain;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 肖龙威
 * @date 2022/05/11 15:36
 */
@Data
@Setter
@Getter
@ConfigurationProperties(prefix = "hello")
@Component
public class Hello {

    public Hello(){

    }

    private String name;

    private Integer age;

    private Integer count;

    private String describe;

    private char[] chars;

}
