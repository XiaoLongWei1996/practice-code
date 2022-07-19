package testspringboot.testspringboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.*;
import testspringboot.testspringboot.domain.Hello;
import testspringboot.testspringboot.domain.Student;

/**
 * @author 肖龙威
 * @date 2022/03/08 14:57
 */
@Configuration
public class MyMVCConfig {

    @Autowired
    private Environment env;

    @Bean
    public Student student(){
        Student student = new Student();
        student.setName("大米");
        return student;
    }

    @Bean
    public Hello hello(Student s){
        Hello h = new Hello();
        h.setName(s.getName());
        return h;
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                WebMvcConfigurer.super.addViewControllers(registry);
            }
        };
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE");
            }
        };
    }
}
