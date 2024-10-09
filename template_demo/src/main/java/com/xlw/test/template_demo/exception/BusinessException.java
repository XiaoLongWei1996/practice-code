package com.xlw.test.template_demo.exception;

/**
 * @description: 业务异常
 * @Title: BusinessException
 * @Author xlw
 * @Package com.sxkj.pay.demos.web.exception
 * @Date 2024/8/7 16:46
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}
