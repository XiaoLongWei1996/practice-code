package com.springcloud.test.alibabaconfig.function;

import com.springcloud.test.alibabaconfig.entity.Dept;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * spring cloud function直接通过url访问方法,不需要再写controller,功能性接口可以使用这种办法
 * @author 肖龙威
 * @date 2022/11/17 17:26
 */
@Component
public class TestFunction {

    @Bean
    public Function<Mono<Integer>, Mono<Integer>> add() {
        return m -> m.map(v -> v * 2);
    }

    
    @Bean
    public Consumer<Flux<Integer>> add1() {
        return f -> {
            f.subscribe(System.out::println);
        };
    }

    @Bean
    public Function<Integer, Integer> add2() {
        return i -> i * 2;
    }

    @Bean
    public Function<Flux<Dept>, Flux<String>> add3() {
        return f -> {
           return f.map(d -> d.getName());
        };
    }
}
