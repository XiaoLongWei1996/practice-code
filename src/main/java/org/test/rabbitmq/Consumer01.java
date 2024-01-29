package org.test.rabbitmq;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * 多个消费者消费同一队列的消息时,是轮询消费的
 * channel.basicConsume()阻塞消费
 * @author 肖龙威
 * @date 2022/07/11 14:10
 */
public class Consumer01 {

    //推送到是队列名称
    private static final String QUEUE_NAME = "test";

    @SuppressWarnings("all")
    public static void main(String[] args) throws IOException {
        Channel channel = MQConnectionUtils.createChannel();
        //成功消费的回调对象
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            //获得消息
            String message = new String(delivery.getBody());
            System.out.println(consumerTag + "===" + message);
        };
        //消费者取消订阅调用，如队列被删除了
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者取消订阅");
        };
        System.out.println("C2等待接收消息");
        //从队列里面获取数据
        channel.basicConsume(
                QUEUE_NAME,  //队列名称
                true,        //自动应答
                deliverCallback,  //成功消费对象
                cancelCallback    //失败消费对象
        );
//        try {
//            channel.close();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }
    }
}
