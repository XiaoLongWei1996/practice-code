package com.test.springboot.controller;


import com.test.springboot.websocket.WebScoketServer;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author 肖龙威
 * @date 2022/07/05 11:02
 */
@RestController
@RequestMapping("/message01")
public class MessageController {

    @Resource
    private RabbitTemplate rabbitTemplate;


    @GetMapping("/test")
    public ResponseEntity<Object> test(String message) {
        WebScoketServer.sendMessage(message, "0001");
        return ResponseEntity.ok(200);
    }

    @PostMapping("send")
    public ResponseEntity<Object> send(String message) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString() + "xlw");
        rabbitTemplate.convertAndSend("ex-delayed", "delay1", message, correlationData);
        return ResponseEntity.ok(200);
    }

}
