package com.springcloud.test.system.config.feign;

/**
 * @author 肖龙威
 * @date 2022/09/22 15:49
 */
public class FailMethod {

    public String retryFail(Exception e) {
        return "请求超时";
    }
}
