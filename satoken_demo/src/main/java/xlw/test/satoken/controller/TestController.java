package xlw.test.satoken.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @Title: TestController
 * @Author xlw
 * @Package xlw.test.controller
 * @Date 2024/3/23 15:54
 */
@Api(tags = "测试")
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    @ApiOperation("t")
    @GetMapping("t")
    public String t() {
        return "hello";
    }
}
