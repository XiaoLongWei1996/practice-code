package com.springcloud.test.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.springcloud.test.system.entity.Dept;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;

import java.util.TreeSet;
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
        TreeSet<Dept> set = new TreeSet<Dept>(((o1, o2) -> {
            return o1.getId().equals(o2.getId()) ? 0 : 1;
        }));
        Dept dept = new Dept();
        dept.setId(1);
        dept.setName("A");
        set.add(dept);
        Dept dept2 = new Dept();
        dept2.setId(2);
        dept2.setName("B");
        set.add(dept2);
        Dept dept3 = new Dept();
        dept3.setId(1);
        dept3.setName("C");
        set.add(dept3);
        System.out.println(set);
    }
}
