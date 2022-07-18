package testspringboot.testspringboot.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import testspringboot.testspringboot.domain.RestResponse;

/**
 * @author 肖龙威
 * @date 2022/03/15 10:22
 */
@RestControllerAdvice
public class ExceptionHandleController {

    @ExceptionHandler
    public RestResponse<Object> exception(Exception e) {
        e.printStackTrace();
        return new RestResponse<>(400, e.getMessage(), null);
    }
}
