package com.springcloud.test.system.controller;

import cn.hutool.core.util.RandomUtil;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;

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
        try {
            int i = 1/0;
            System.out.println("执行");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("后执行");
    }
}
