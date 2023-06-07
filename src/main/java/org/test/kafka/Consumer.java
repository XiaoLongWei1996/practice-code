package org.test.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * MQ消费者消费消息有两种模式：
 * 1、broker向消费者push，消费者被动消费，这样缺点是各个消费者的消费能力不同，导致消费者不能很好的处理数据；
 * 2、消费者向broker pull数据，这样每个消费者都能根据自身性能进行处理数据。
 * @className: Consumer
 * @author: xlw
 * @date: 2023/5/31 14:32
 **/
public class Consumer {

    /**
     * kafka可以一个消费者进行消费broker，也可以多个消费者组成消费者组进行消费broker，消费者组中的每个消费者可以消费1个或多个topic分区，1个分区只能被一个消费者消费
     * consumer采用从broker中主动拉取数据。
     * 消费者组的默认分区分配策略是：Range + CooperativeSticky，分区按照顺序平均分给消费者，如果分区%消费者组数 > 0，那么前几个消费者会多消费分区，容易造成数据倾斜。
     */
    private static void createConsumer() {
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

    /**
     * 消费者消费broker里面的数据，broker并不会消费完就删除掉数据，所以为了避免重复消费，broker里面为每一个消费者维护了一个offset（相当于指针）
     * 消费者消费完一条数据，offset就会偏移，默认情况下offset是自动偏移的，消费者只要拉取到消息立马就会偏移
     */
    private static void createOffsetConsumer() {
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
        //配置自动提交 offset
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // 配置提交offset间隔1s
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
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
     * 消费者消费broker里面的数据，broker并不会消费完就删除掉数据，所以为了避免重复消费，broker里面为每一个消费者维护了一个offset（相当于指针）
     * 消费者消费完一条数据，offset就会偏移，默认情况下offset是自动偏移的，消费者只要拉取到消息立马就会偏移
     * 手动提交：自动提交无法确认消息一定被消费者消费，这时候可以使用手动提交，手动提交分为同步提交(commitSync)和异步提交(commitAsync)；
     * commitSync：必须等待offset提交完毕，再去消费下一批数据，同步提交阻塞当前线程，一直到提交成功，并且会自动失败重试（由不可控因素导致，也会出现提交失败）；
     * commitAsync：发送完提交offset请求后，就开始消费下一批数据了，故有可能提交失败。
     */
    private static void createCommitSyncConsumer() {
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
        //配置自动提交 offset
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 配置提交offset间隔1s
        //properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
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
            //同步提交，性能差，数据一致性好
            consumer.commitSync();
        }
        //        consumer.close();
    }

    /**
     * 消费者消费broker里面的数据，broker并不会消费完就删除掉数据，所以为了避免重复消费，broker里面为每一个消费者维护了一个offset（相当于指针）
     * 消费者消费完一条数据，offset就会偏移，默认情况下offset是自动偏移的，消费者只要拉取到消息立马就会偏移
     * 手动提交：自动提交无法确认消息一定被消费者消费，这时候可以使用手动提交，手动提交分为同步提交(commitSync)和异步提交(commitAsync)；
     * commitSync：必须等待offset提交完毕，再去消费下一批数据，同步提交阻塞当前线程，一直到提交成功，并且会自动失败重试（由不可控因素导致，也会出现提交失败）；
     * commitAsync：发送完提交offset请求后，就开始消费下一批数据了，故有可能提交失败。
     */
    private static void createCommitAsyncConsumer() {
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
        //配置自动提交 offset
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 配置提交offset间隔1s
        //properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
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
            //异步提交，性能好，数据一致性差
            consumer.commitAsync(new OffsetCommitCallback(){
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                    //异步提交回调函数
                    if (e != null) {
                        System.out.println("提交失败");
                        e.printStackTrace();
                    }
                    System.out.println(map);
                }
            });
        }
        //        consumer.close();
    }

    /**
     * 指定消费者从某个偏移量开始进行消费；auto.offset.reset = earliest | latest | none 默认是 latest。
     * earliest：自动将偏移量重置为最早的偏移量，--from-beginning。
     * latest（默认值）：自动将偏移量重置为最新偏移量。
     * none：如果未找到消费者组的先前偏移量，则向消费者抛出异常。
     */
    private static void createSetOffsetConsumer() {
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
        //properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, StickyAssignor.class.getName());
        // 配置自动提交 offset
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // 配置提交offset间隔1s
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
        // 配置偏移量
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
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
     * 消费者指定具体的Offset位置开始消费
     */
    private static void createSetOffsetConsumer01() {
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
        //properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, StickyAssignor.class.getName());
        // 配置自动提交 offset
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // 配置提交offset间隔1s
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
        // 配置偏移量
        //properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        // 注册要消费的主题（可以消费多个主题）
        List<String> topics = new ArrayList<>();
        topics.add(topicName);
        consumer.subscribe(topics);
        //获取topic所有的分区
        Set<TopicPartition> assignment = consumer.assignment();
        for (TopicPartition topicPartition : assignment) {
            //指定消费偏移量
            consumer.seek(topicPartition, 500);
        }
        while (true) {
            ConsumerRecords<String, String> data = consumer.poll(Duration.ofMillis(2000));
            for (ConsumerRecord<String, String> cr : data) {
                System.out.println(cr);
            }
        }
        //        consumer.close();
    }

    public static void main(String[] args) {
        createSetOffsetConsumer01();
    }
}
