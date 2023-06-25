package com.xlw.kafka_demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @className: Consumer
 * @author: xlw
 * @date: 2023/6/7 17:55
 **/
@Component
public class Consumer {

//    @KafkaListener(topics = "topic-1", groupId = "g1")
//    public void acquire(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        System.out.println("消费者1消费：" + record);
//        //手动签收
//        ack.acknowledge();
//    }

    @KafkaListener(topics = "topic-1", groupId = "g2", concurrency = "2")
    public void acquire2(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        System.out.println("消费者2消费：" + records);
        //手动签收
        ack.acknowledge();
    }

    @KafkaListener(topics = "topic-1", groupId = "g2", concurrency = "2")
    public void acquire3(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        System.out.println("消费者3消费：" + records);
        //手动签收
        ack.acknowledge();
    }
}
