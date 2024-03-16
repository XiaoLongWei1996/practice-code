package xlw.test.dynamicthreadpoll_demo.config;

import lombok.Data;

/**
 * @description:
 * @Title: Result
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.entity
 * @Date 2023/12/23 19:46
 */
@Data
public class Result<T> {

    private Integer code;

    private T body;

    private String message;

    public Result(Integer code, T body, String message) {
        this.code = code;
        this.body = body;
        this.message = message;
    }

    public static <T> Result succeed(T t) {
        return new Result(200,t, "成功");
    }

    public static <T> Result fail(T t) {
        return new Result(400, t, "失败");
    }
}
