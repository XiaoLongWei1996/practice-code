package testspringboot.testspringboot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testspringboot.testspringboot.listener.MyEvent;

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
    public ResponseEntity<String> test(String message) {
        MyEvent event = new MyEvent(message);
        applicationContext.publishEvent(event);
        System.out.println("执行结束");
        return ResponseEntity.ok("ok");
    }
}
