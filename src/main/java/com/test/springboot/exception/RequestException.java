package com.test.springboot.exception;

/**
 * @author 肖龙威
 * @date 2022/03/15 10:21
 */
public class RequestException extends RuntimeException {
    public RequestException(String msg) {
        super(msg);
    }

    public RequestException() {
        super("拒绝请求，参数错误");
    }
}
