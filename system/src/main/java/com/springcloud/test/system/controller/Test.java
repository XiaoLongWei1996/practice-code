package com.springcloud.test.system.controller;

import cn.hutool.core.util.RandomUtil;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

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

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService pool = Executors.newFixedThreadPool(16);
        //CompletableFuture<Void> hello = CompletableFuture.runAsync(() -> System.out.println("hello"));
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello world");
            return "hello world";
        }, pool);

        CompletableFuture<String> cf2 = cf.exceptionally(e -> {
            return e.getMessage();
        });

        CompletableFuture<String> cf1 = cf.thenApplyAsync(String::toUpperCase, pool);
        //System.out.println(cf.get(3, TimeUnit.SECONDS));
        //System.out.println(cf2.get());
        //System.out.println(cf1.get());
        pool.shutdown();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> System.out.print("hello"));
    }

    public void test() {

    }
}
