package org.springcloud.test.function.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 测试springcloud function
 * @author 肖龙威
 * @date 2023/03/15 10:53
 */
@Component
public class MyFunction {

    /**
     * 访问格式:post url + /uppercase
     * @return
     */
    @Bean
    public Function<String, String> uppercase() {
        return s -> s.toUpperCase();
    }

    /**
     * 访问格式:get url + /randomWord
     * @return
     */
    @Bean
    public Supplier<String> randomWord() {
        return () -> UUID.randomUUID().toString();
    }

    /**
     * 访问格式: post url + /lowercase
     * @return
     */
    @Bean
    public Function<Mono<String>, Mono<String>> lowercase() {
        return mono -> mono.map(value -> value.toLowerCase());
    }

    /**
     * 访问格式: post url + /handle
     * @return
     */
    @Bean
    public Consumer<Flux<Integer>> handle() {
        return f -> f.buffer(2).subscribe(System.out::println);
    }

    /**
     * 访问格式: post url + /handle1
     * @return
     */
    @Bean
    public Consumer<Mono<String>> handle1() {
        return m -> m.concatWith(Mono.error(new Exception())).doOnError(e -> e.printStackTrace()).subscribe(System.out::println);
    }

}
