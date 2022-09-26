package com.springcloud.test.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 肖龙威
 * @date 2022/09/23 14:49
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ApiModel("响应对象")
public class Result<T> {

    @ApiModelProperty(value = "响应码")
    private Integer code;

    @ApiModelProperty(value = "响应内容")
    private T body;

    @ApiModelProperty(value = "响应信息")
    private String message;

    public static <T> Result<T> ok(T t) {
        Result<T> result = new Result<>();
        result.setMessage("请求成功");
        result.setCode(200);
        result.setBody(t);
        return result;
    }

    public static Result fail(String e) {
        return Result.builder().code(400).body(e).message("请求失败").build();
    }
}
