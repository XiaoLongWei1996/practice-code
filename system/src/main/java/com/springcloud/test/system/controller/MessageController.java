package com.springcloud.test.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.springcloud.test.system.entity.Result;
import entity.GloableMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 肖龙威
 * @date 2022/10/25 14:20
 */
@Api(tags = "消息")
@RestController
@RequestMapping("message")
public class MessageController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @ApiOperation(value = "发送", notes = "发送")
    @PostMapping("send")
    public ResponseEntity<Result<String>> send(String msg){
        GloableMessage message = new GloableMessage();
        message.setId(RandomUtil.randomLong());
        message.setBody(msg);
        rabbitTemplate.convertAndSend("msg", message);
        return ResponseEntity.ok(Result.ok(msg));
    }

}
