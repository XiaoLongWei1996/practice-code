package org.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * @author 肖龙威
 * @date 2022/07/13 16:03
 */
public class TempConsumer01 {

    private static final String EXCHANGE_NAME = "log01";

    @SuppressWarnings("all")
    public static void main(String[] args) throws IOException {
    }

    //direct交换机
    private static void directExchange() throws IOException {
        Channel channel = MQConnectionUtils.createChannel();
        //创建direct交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "erro");
        channel.basicConsume(queueName, true
                , ((consumerTag, message) -> {
            String s = new String(message.getBody());
                    System.out.println(s);
        }), consumerTag -> {});
    }

    //fanout交换机
    private static void fanoutExchange() throws IOException {
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
