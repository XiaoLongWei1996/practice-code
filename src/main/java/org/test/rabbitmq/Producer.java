package org.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq生产者
 *
 * @author 肖龙威
 * @date 2022/07/11 11:09
 */
public class Producer {

    //推送到是队列名称
    private static final String QUEUE_NAME = "test";

    @SuppressWarnings("all")
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");
        //通过连接工厂创建连接
        Connection connection = factory.newConnection();
        //通过连接创建通道(多线程应用,多个可以公用一个连接,数据传输通过通道来交互)
        Channel channel = connection.createChannel();
        //创建一个队列
        channel.queueDeclare(
                QUEUE_NAME, //队列的名称
                false,   //队列李的数据是否需要持久化(默认储存在内存中)
                false,   //该队列是否共享(true:共享,可以多个消费者消费;false不共享,只能被一个消费者消费),
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
                    null,  //其它参数信息
                    message.getBytes()  //发送的消息
            );
        }
        System.out.println("发送完毕");
        //关闭通道
        channel.close();
        //关闭连接
        connection.close();
    }
}
