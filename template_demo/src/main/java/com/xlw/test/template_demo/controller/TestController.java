package com.xlw.test.template_demo.controller;

import com.xlw.test.template_demo.entity.Student;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * @description:
 * @Title: TestController
 * @Author xlw
 * @Package com.sxkj.pay.controller
 * @Date 2024/9/29 15:12
 */
@RestController
@RequestMapping("test")
public class TestController {

    @PostMapping(value = "t1", consumes = "application/x-www-form-urlencoded")
    public String t1(@RequestParam Map<String, String> params) {
        return params.toString();
    }

    /**
     * T2
     *
     * @param a 一个
     * @param b b
     * @return {@link BigDecimal }
     */
    @GetMapping("t2")
    public BigDecimal t2(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    /**
     * T3
     *
     * @param a 一个
     * @return {@link LocalDateTime }
     */
    @GetMapping("t3")
    public LocalDateTime t3(LocalDateTime a) {
        return a;
    }

    @GetMapping("t4")
    public LocalDate t4(LocalDate a) {
        return a;
    }

    @GetMapping("t5")
    public Date t5(Date a) {
        return a;
    }

    @PostMapping("t6")
    public Student t6(@RequestBody Student student) {
        return student;
    }

}
