package org.test.rabbitmq;

import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * 临时队列:通过String queueName = channel.queueDeclare().getQueue()创建一个临时的队列
 *         当该队列没有任何的消费者后,队列会被删除。
 * @author 肖龙威
 * @date 2022/07/13 15:50
 */
public class TempConsumer {

    private static final String EXCHANGE_NAME = "log";

    @SuppressWarnings("all")
    public static void main(String[] args) throws IOException {
        Channel channel = MQConnectionUtils.createChannel();
        //创建具名的临时队列
        String queueName = channel.queueDeclare().getQueue();
        //创建交换机
        channel.exchangeDeclare(
                EXCHANGE_NAME    //交换机名称
                , "fanout"       //交换机的类型
        );
        //队列绑定交换机
        channel.queueBind(
                queueName    //队列名称
                , EXCHANGE_NAME //交换机名称
                , ""  //路由key
                      //把该临时队列绑定我们的 exchange 其中 routingkey(也称之为 binding key)为空字符串
        );
        channel.basicConsume(queueName, true
                ,(consumerTag, delivery) -> {
                    String message = new String(delivery.getBody());
                    System.out.println(message);
                }
                , (consumerTag) -> {
                    System.out.println("消费失败");
                });
    }
}
