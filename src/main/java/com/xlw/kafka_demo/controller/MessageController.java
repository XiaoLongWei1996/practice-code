package com.xlw.kafka_demo.controller;

import com.xlw.kafka_demo.entity.Result;
import com.xlw.kafka_demo.entity.Student;
import com.xlw.kafka_demo.exception.ResultException;
import com.xlw.kafka_demo.kafka.Producer;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @className: MessageController
 * @author: xlw
 * @date: 2023/6/7 18:00
 **/
@Validated
@RequestMapping("message")
@RestController
public class MessageController {

    @Resource
    private Producer producer;

    @GetMapping("sent")
    public String sent(String key, String value) {
        producer.sentMessage(key, value);
        return "发送成功";
    }

    @GetMapping("sentSingle")
    public String sentSingle(String value) {
        producer.sentMessage(value);
        return "发送成功";
    }

    @PostMapping("createStudent")
    public Result<Student> createStudent(@Validated Student student) {
        if (student.getId() % 2 == 0) {
            throw ResultException.create(new Student());
        }
        return Result.succeed(student);
    }



    @PostMapping("updateStudent")
    public Result<Student> updateStudent(
            @Valid @NotNull Integer id
            , String name
            , Integer age) {
        System.out.println(id);
        return Result.succeed("ok");
    }

    @PostMapping("deleteStudent")
    public Result<Student> deleteStudent(@Validated @RequestBody Student student) {
        return Result.succeed(student);
    }
}
