package org.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 手动应答只能保证在消费的过程中,消息不会丢失;一旦mq宕机,那么队列就会消失,这样的话会导致大量的数据丢失;
 * 持久化队列,把队列持久化到磁盘中,即使mq宕机,重启后队列依旧存在;
 * 注意:之前已存在非持久化的队列不能再次被持久化,除非删除重建
 * 持久化消息:要想mq宕机消息不会消失,那么生产者生产的消息也必须持久化;但是这种持久化并不能一定会把消息
 * 写入到磁盘中,当正在写入磁盘还未写完的时候宕机,那么该消息还是会丢失。
 * @author 肖龙威
 * @date 2022/07/11 16:04
 */
public class DurableProducer {

    private static final String QUEUE_NAME = "durable_test";

    @SuppressWarnings("all")
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MQConnectionUtils.createChannel();
        //是否持久化
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, //队列名称
                durable,   //持久化队列
                false,     //该队列是否共享(true:共享,可以多个消费者消费;false不共享,只能被一个消费者消费),
                false,   //是否自动删除队列,true最后一个消费者断开连接,该队列也会被删除,
                null     //其它参数
                );
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            //需要发送到队列中的消息
            String message = scanner.next();
            channel.basicPublish(
                    "", //发送到那个交换机
                    QUEUE_NAME, //路由key(也就是队列名称)
                    MessageProperties.PERSISTENT_TEXT_PLAIN, //持久化消息
                    message.getBytes()  //发送的消息
            );
        }
        System.out.println("发送完毕");
        //关闭通道
        channel.close();
    }
}
