package org.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @className: Consumer
 * @author: xlw
 * @date: 2023/5/31 14:32
 **/
public class Consumer {

    private static void createConsumer() {
        // topic名称
        String topicName = "test";
        // kafka消费者配置属性
        Properties properties = new Properties();
        // 指定kafka服务
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.220.99.163:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "g1");
        //创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        Set<String> topicSet = new HashSet<>();
        topicSet.add(topicName);
        consumer.subscribe(topicSet);
        while (true) {
            ConsumerRecords<String, String> data = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> cr : data) {
                System.out.println(cr);
            }
        }
//        consumer.close();
    }

    public static void main(String[] args) {
        createConsumer();
    }
}
