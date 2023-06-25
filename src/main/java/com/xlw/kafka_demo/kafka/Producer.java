package com.xlw.kafka_demo.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

/**
 * @className: Producer
 * @author: xlw
 * @date: 2023/6/7 16:44
 **/
@Component
public class Producer {

    @Resource
    private KafkaTemplate<String, String> template;

    @Transactional
    public void sentMessage(String key, String value) {
        ListenableFuture<SendResult<String, String>> send = template.send("topic-1", key, value);
        send.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("消息发送失败");
                ex.printStackTrace();
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("消息发送成功");
                System.out.println(result);
            }
        });
    }

    public void sentMessage(String value) {
        //事务发送
        template.executeInTransaction(operate -> {
            return operate.send("topic-1", value);
        });
    }
}
