package org.test.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * @className: Producer
 * @author: xlw
 * @date: 2023/5/30 16:51
 */
public class Producer {

    /**
     * 创建普通生产商
     */
    private static void createProducer() {
        // topic名称
        String topicName = "test";
        // kafka生产者配置属性
        Properties properties = new Properties();
        // 指定kafka服务
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.220.99.163:9092");
        // 序列化key
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 系列化值
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 配置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 配置重试间隔时间
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
        // 配置RecordAccumulator 缓冲区总大小
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // 配置缓冲区,一次达到多大的消息开始发送，16384代表16k。
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 配置提交间隔,控制消息发送延时行为的，该参数默认值是0。表示消息需要被立即发送，无须关系batch是否被填满。
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        // 配置broker应答
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        // 配置压缩类型
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        // kafka生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String word = scanner.next();
            if ("exit".equals(word)) {
                System.out.println("生产者退出服务");
                break;
            }
            // 发送数据
            producer.send(new ProducerRecord<>(topicName, word));
            producer.flush();
        }
        producer.close(Duration.ofSeconds(3));
    }

    /**
     * 创建回调生产者,消息发送给broker,broker会发送给生产者一条ack消息,并调用生产者的回调函数
     */
    private static void createCallbackProducer() {
        // topic名称
        String topicName = "test";
        // kafka生产者配置属性
        Properties properties = new Properties();
        // 指定kafka服务
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.220.99.163:9092");
        // 序列化key
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 系列化值
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 配置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 配置重试间隔时间
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
        // 配置RecordAccumulator 缓冲区总大小
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // 配置缓冲区,一次达到多大的消息开始发送，16384代表16k。
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 配置提交间隔,控制消息发送延时行为的，该参数默认值是0。表示消息需要被立即发送，无须关系batch是否被填满。
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        // 配置broker应答
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        // 配置压缩类型
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        // kafka生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        // 发送数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String word = scanner.next();
            if ("exit".equals(word)) {
                System.out.println("生产者退出服务");
                break;
            }
            producer.send(
                    new ProducerRecord<>(topicName, word),
                    //消息发送回调函数，Exception为空正常发送，否则异常
                    new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                            if (e == null) {
                                System.out.println("主题：" + recordMetadata.topic() + "-> 分区：" + recordMetadata.partition());
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
            producer.flush();
        }
        producer.close(Duration.ofSeconds(3));
    }

    /**
     * 生产者同步发送，kafka生产者默认是异步发送，消息发送给broker无须等待ack就可以发送第二条消息；
     * 同步发送，kafka生产者必须等待前一条数据被ack才能继续发送数据
     */
    private static void createSyncProducer() {
        // topic名称
        String topicName = "test";
        // kafka生产者配置属性
        Properties properties = new Properties();
        // 指定kafka服务
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.220.99.163:9092");
        // 序列化key
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 系列化值
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 配置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 配置重试间隔时间
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
        // 配置RecordAccumulator 缓冲区总大小
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // 配置缓冲区,一次达到多大的消息开始发送，16384代表16k。
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 配置提交间隔,控制消息发送延时行为的，该参数默认值是0。表示消息需要被立即发送，无须关系batch是否被填满。
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        // 配置broker应答
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        // 配置压缩类型
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        // kafka生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        // 发送数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String word = scanner.next();
            if ("exit".equals(word)) {
                System.out.println("生产者退出服务");
                break;
            }
            try {
                //同步发送,在异步的基础上加上get()
                RecordMetadata recordMetadata = producer.send(new ProducerRecord<>(topicName, word)).get();
                System.out.println(recordMetadata);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            producer.flush();
        }
        producer.close(Duration.ofSeconds(3));
    }

    /**
     * 创建生产者,指定发送数据到topic的某个分区中
     */
    private static void createPartitionProducer() {
        // topic名称
        String topicName = "test";
        // kafka生产者配置属性
        Properties properties = new Properties();
        // 指定kafka服务
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.220.99.163:9092");
        // 序列化key
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 系列化值
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 配置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 配置重试间隔时间
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
        // 配置RecordAccumulator 缓冲区总大小
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // 配置缓冲区,一次达到多大的消息开始发送，16384代表16k。
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 配置提交间隔,控制消息发送延时行为的，该参数默认值是0。表示消息需要被立即发送，无须关系batch是否被填满。
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        // 配置broker应答
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        // 配置压缩类型
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        // kafka生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        // 发送数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String word = scanner.next();
            if ("exit".equals(word)) {
                System.out.println("生产者退出服务");
                break;
            }
            producer.send(
                    //数据指定发送到topic的第二个分区
                    new ProducerRecord<>(topicName, 2, "", word),
                    //消息发送回调函数，Exception为空正常发送，否则异常
                    new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                            if (e == null) {
                                System.out.println("主题：" + recordMetadata.topic() + "-> 分区：" + recordMetadata.partition());
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
            producer.flush();
        }
        producer.close(Duration.ofSeconds(3));
    }

    /**
     * 创建生产者,将 key 的 hash 值与 topic 的 partition 数进行取余得到 partition 值
     *
     */
    private static void createKeyProducer() {
        // topic名称
        String topicName = "test";
        // kafka生产者配置属性
        Properties properties = new Properties();
        // 指定kafka服务
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.220.99.163:9092");
        // 序列化key
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 系列化值
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 配置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 配置重试间隔时间
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
        // 配置RecordAccumulator 缓冲区总大小32M
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // 配置缓冲区,一次达到多大的消息开始发送，16384代表16k。
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 配置提交间隔,控制消息发送延时行为的，该参数默认值是0。表示消息需要被立即发送，无须关系batch是否被填满。
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        // 配置broker应答,0：生产者发送过来的数据，不需要等数据落盘应答;1：生产者发送过来的数据，Leader收到数据后应答;-1（all）：生产者发送过来的数据，Leader+和isr队列里面的所有节点收齐数据后应答。-1和all等价。
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        // 配置压缩类型,默认 none，可配置值 gzip、snappy、lz4 和 zstd
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        // kafka生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        // 发送数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String word = scanner.next();
            if ("exit".equals(word)) {
                System.out.println("生产者退出服务");
                break;
            }
            producer.send(
                    //数据指定key,通过key的hash取模发送数据到某分区
                    new ProducerRecord<>(topicName, word, word),
                    //消息发送回调函数，Exception为空正常发送，否则异常
                    new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                            if (e == null) {
                                System.out.println("主题：" + recordMetadata.topic() + "-> 分区：" + recordMetadata.partition());
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
            producer.flush();
        }
        producer.close(Duration.ofSeconds(3));
    }

    /**
     * 创建事务生产者，生产者使用事务保证幂等性
     * 说明：开启事务，必须开启幂等性。
     */
    private static void createTransactionProducer() {
        // topic名称
        String topicName = "test";
        // kafka生产者配置属性
        Properties properties = new Properties();
        // 指定kafka服务
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.220.99.163:9092");
        // 序列化key
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 系列化值
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 配置重试次数，控制消息只发送一次
        properties.put(ProducerConfig.RETRIES_CONFIG, 1);
        // 配置重试间隔时间
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
        //配置幂等性
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        //配置事务id,设置事务 id（必须），事务 id 任意起名
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transaction_0");
        // 配置RecordAccumulator 缓冲区总大小32M
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // 配置缓冲区,一次达到多大的消息开始发送，16384代表16k。
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 配置提交间隔,控制消息发送延时行为的，该参数默认值是0。表示消息需要被立即发送，无须关系batch是否被填满。
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        // 配置broker应答,0：生产者发送过来的数据，不需要等数据落盘应答;1：生产者发送过来的数据，Leader收到数据后应答;-1（all）：生产者发送过来的数据，Leader+和isr队列里面的所有节点收齐数据后应答。-1和all等价。
        properties.put(ProducerConfig.ACKS_CONFIG, "-1");
        // 配置压缩类型,默认 none，可配置值 gzip、snappy、lz4 和 zstd
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        // kafka生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        //初始化事务
        producer.initTransactions();
        //开启事务
        producer.beginTransaction();
        // 发送数据
        try {
            for (int i = 0; i < 5; i++) {
                producer.send(new ProducerRecord<>(topicName, "xlw" + i));
            }
            int i = 1 / 0;
            //提交事务
            producer.flush();
            producer.commitTransaction();
        } catch (Exception e) {
            //回滚事务
            producer.abortTransaction();
            e.printStackTrace();
        } finally {
            producer.close(Duration.ofSeconds(3));
        }
    }

    public static void main(String[] args) {
        Producer.createKeyProducer();
    }
}
