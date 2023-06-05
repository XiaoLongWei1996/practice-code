package org.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * @className: Consumer
 * @author: xlw
 * @date: 2023/5/31 14:32
 **/
public class Consumer {

    /**
     * consumer采用从broker中主动拉取数据。
     */
    private static void createConsumer() {
        // topic名称
        String topicName = "test";
        // kafka消费者配置属性
        Properties properties = new Properties();
        // 指定kafka服务
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "xlw.asia:9092");
        // 消费者反序列化key
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 消费者反序列化value
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 指定消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "g1");
        // 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        // 注册要消费的主题（可以消费多个主题）
        Set<String> topicSet = new HashSet<>();
        topicSet.add(topicName);
        consumer.subscribe(topicSet);
        while (true) {
            ConsumerRecords<String, String> data = consumer.poll(Duration.ofMillis(2000));
            for (ConsumerRecord<String, String> cr : data) {
                System.out.println(cr);
            }
        }
//        consumer.close();
    }

    /**
     * 指定consumer消费broker中指定的分区
     */
    private static void createPartitionConsumer() {
        // topic名称
        String topicName = "test";
        // kafka消费者配置属性
        Properties properties = new Properties();
        // 指定kafka服务
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "xlw.asia:9092");
        // 消费者反序列化key
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 消费者反序列化value
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 指定消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "g1");
        // 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        // 指定消费主题以及分区
        List<TopicPartition> partitions = new ArrayList<>();
        partitions.add(new TopicPartition(topicName, 0));
        partitions.add(new TopicPartition(topicName, 1));
        consumer.assign(partitions);
        while (true) {
            ConsumerRecords<String, String> data = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> cr : data) {
                System.out.println(cr);
            }
        }
        //        consumer.close();
    }

    public static void main(String[] args) {
        createPartitionConsumer();
    }
}
