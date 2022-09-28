package com.springcloud.test.system.controller;

import cn.hutool.core.util.RandomUtil;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 * @author 肖龙威
 * @date 2022/09/23 23:01
 */
@Component
public class Test {

    @TimeLimiter(name = "timeA", fallbackMethod = "myTimeout")
    public CompletableFuture<String> getToken() throws InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RandomUtil.randomString(8);
        });
    }

    public CompletableFuture<String> myTimeout(Exception e) {
        e.printStackTrace();
        return CompletableFuture.supplyAsync(() -> "请求超时");
    }

    public static void main(String[] args) {
        Mono<Integer> just = Mono.empty();

        just.subscribe(System.out::println);
        just = Mono.just(12);
        InputStream in = System.in;
        Scanner scanner = new Scanner(in);
        scanner.next();
//        System.out.println(1111);
        //System.out.println(just.block());
        //just.subscribe(System.out::println);

    }
}
