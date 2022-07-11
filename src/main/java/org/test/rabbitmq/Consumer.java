package org.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitMQ消费者
 * @author 肖龙威
 * @date 2022/07/11 13:29
 */
public class Consumer {

    //推送到是队列名称
    private static final String QUEUE_NAME = "test";

    @SuppressWarnings("all")
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");
        //创建连接
        Connection connection = factory.newConnection();
        //创建管道
        Channel channel = connection.createChannel();
        //成功消费的回调对象
        DeliverCallback deliverCallback = (consumerTag,  //消费者的tag信息(相当于消费者的id)
                                           delivery      //信息的数据对象,包含数据和消息的tag信息
        ) -> {
            //获得消息
            String message = new String(delivery.getBody());
            System.out.println(message);
        };
        //失败消费的回调函数(在消费的时候队列被删除)
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("数据消费失败");
        };
        System.out.println("C1等待接收消息");
        //从队列里面获取数据,阻塞消费
        channel.basicConsume(
                QUEUE_NAME,  //队列名称
                true,        //自动应答
                deliverCallback,  //成功消费对象
                cancelCallback    //失败消费对象
        );
    }
}
