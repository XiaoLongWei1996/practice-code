package com.test.springboot.controller;


import com.test.springboot.websocket.WebScoketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 肖龙威
 * @date 2022/07/05 11:02
 */
@RestController
@RequestMapping("/message01")
public class MessageController {


    @Autowired
    private ApplicationContext applicationContext;


    @GetMapping("/test")
    public ResponseEntity<Object> test(String message) {
        WebScoketServer.sendMessage(message, "0001");
        return ResponseEntity.ok(200);
    }
}
