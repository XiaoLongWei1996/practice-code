package testspringboot.testspringboot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testspringboot.testspringboot.domain.MyMessage;
import testspringboot.testspringboot.rabbitmq.Producer;

/**
 * @author 肖龙威
 * @date 2022/07/05 11:02
 */
@RestController
@RequestMapping("/message01")
public class MessageController {


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Producer producer;

    @GetMapping("/test")
    public ResponseEntity<Object> test(String message) {
        MyMessage m = new MyMessage();
        m.setBody(message);
        m.setCode(200);
        producer.send(m, 5000);
        return ResponseEntity.ok(200);
    }
}
