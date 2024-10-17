package com.xlw.test.template_demo.controller;

import com.xlw.test.template_demo.entity.Student;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;


/**
 * @module 测试
 * @author Xlw
 * @date 2024/10/17
 */
@RestController
@RequestMapping("test")
public class TestController {

    /**
     * @param params key-value入参
     * @return {@link String }
     */
    @PostMapping(value = "t1", consumes = "application/x-www-form-urlencoded")
    public String t1(@RequestParam Map<String, String> params) {
        return params.toString();
    }


    /**
     * 求和
     * @param a 参数a
     * @param b 阐述b
     * @return {@link BigDecimal } 总和
     */
    @GetMapping("t2")
    public BigDecimal t2(@RequestParam(required = true) BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    /**
     * 日期格式化
     * @param a 参数a
     * @return {@link LocalDateTime }
     */
    @GetMapping("t3")
    public LocalDateTime t3(@RequestParam(required = true) LocalDateTime a) {
        return a;
    }

    @GetMapping("t4")
    public LocalDate t4(LocalDate a) {
        return a;
    }

    /**
     * @deprecated
     * @param a 日期
     * @return {@link Date }
     */
    @Deprecated
    @GetMapping("t5")
    public Date t5(Date a) {
        return a;
    }

    /**
     * 获取对象
     * @param student 学生对象
     * @return {@link Student }
     */
    @PostMapping("t6")
    public Student t6(@RequestBody Student student) {
        return student;
    }

}
