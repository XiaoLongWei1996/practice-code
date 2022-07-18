package testspringboot.testspringboot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 肖龙威
 * @date 2022/03/15 10:23
 */
@Data
@AllArgsConstructor
public class RestResponse<T> {

    private int code;
    private String message;
    private T data = null;

    public RestResponse(T data) {
        this.code = 200;
        this.message = "请求成功";
        this.data = data;
    }


}
