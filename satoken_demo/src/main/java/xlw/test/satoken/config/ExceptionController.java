package xlw.test.satoken.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: 异常处理
 * @Title: ExceptionController
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.controller
 * @Date 2024/1/31 15:35
 */
@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BusinessException.class)
    public Result<String> exceptionHandler(BusinessException e) {
        log.error("程序错误", e);
        return Result.fail(e.getMessage());
    }
}
