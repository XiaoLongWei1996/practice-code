package org.test.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * @className: Consumer01
 * @author: xlw
 * @date: 2023/6/5 17:17
 **/
public class Consumer01 {

    /**
     * consumer采用从broker中主动拉取数据。
     */
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
        List<String> topics = new ArrayList<>();
        topics.add(topicName);
        consumer.subscribe(topics);
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
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.220.99.163:9092");
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
        partitions.add(new TopicPartition(topicName, 2));
        consumer.assign(partitions);
        while (true) {
            ConsumerRecords<String, String> data = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> cr : data) {
                System.out.println(cr);
            }
        }
        //        consumer.close();
    }

    /**
     * RoundRobin：轮询消费把所有的 partition 和所有的
     * consumer 都列出来，然后按照 hashcode 进行排序，最后
     * 通过轮询算法来分配 partition 给到各个消费者。
     */
    private static void createRoundRobinConsumer() {
        // topic名称
        String topicName = "test";
        // kafka消费者配置属性
        Properties properties = new Properties();
        // 指定kafka服务
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.220.99.163:9092");
        // 消费者反序列化key
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 消费者反序列化value
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 指定消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "g1");
        // 设置消费策略
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
        // 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        // 注册要消费的主题（可以消费多个主题）
        List<String> topics = new ArrayList<>();
        topics.add(topicName);
        consumer.subscribe(topics);
        while (true) {
            ConsumerRecords<String, String> data = consumer.poll(Duration.ofMillis(2000));
            for (ConsumerRecord<String, String> cr : data) {
                System.out.println(cr);
            }
        }
        //        consumer.close();
    }

    /**
     * Sticky：首先会尽量均衡的放置分区到消费者上面，在出现同一消费者组内消费者出现问题的时候，会尽量保持原有分配的分区不变化
     */
    private static void createStickyRobinConsumer() {
        // topic名称
        String topicName = "test";
        // kafka消费者配置属性
        Properties properties = new Properties();
        // 指定kafka服务
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.220.99.163:9092");
        // 消费者反序列化key
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 消费者反序列化value
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 指定消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "g1");
        // 设置消费策略
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, StickyAssignor.class.getName());
        // 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        // 注册要消费的主题（可以消费多个主题）
        List<String> topics = new ArrayList<>();
        topics.add(topicName);
        consumer.subscribe(topics);
        while (true) {
            ConsumerRecords<String, String> data = consumer.poll(Duration.ofMillis(2000));
            for (ConsumerRecord<String, String> cr : data) {
                System.out.println(cr);
            }
        }
        //        consumer.close();
    }

    public static void main(String[] args) {
        createStickyRobinConsumer();
    }
}
