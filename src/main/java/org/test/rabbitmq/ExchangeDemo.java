package org.test.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;

/**
 * Exchange(交换机):生产者把消息发送给mq不是直接添加到mq的队列中，而是由交换机接收到信息，再由交换机通过routingKey决定是发送到那个队列，还是
 * 发送给多个队列。
 * Bindings(绑定):将交换机与那个或者那几个队列进行绑定,每一组交换机==队列进行绑定的时候都必须指定一个routingKey。
 * 交换机类型:
 * fanout(扇形):该类型的交换机会把消费者发送的消息分发给绑定的每一个队列,也就是说路由键在扇形交换机里没有作用，故消息队列绑定扇形交换机时，路由键可为空字符串;
 * direct(直接):该类型的交换机会把消费者发送的消息根据指定的routingKey分发给该路由键绑定的队列中去,相当于路由键跟队列是一对一的关系;但是该交换机绑定队列的路
 * 由键可以重名,如果重名分发消息就像fanout;
 * topic(主题):该类交换机会把消费者发送的消息根据routingKey通配符的方式比对,分发到对应的队列去,类似redis的psubscribe和publish,发布订阅模式。
 * 消费者发送路由键xlw.users会匹配绑定*.users的队列。
 *
 * @author 肖龙威
 * @date 2022/07/13 15:37
 */
public class ExchangeDemo {

    private static final String EXCHANGE_NAME = "log";

    @SuppressWarnings("all")
    public static void main(String[] args) throws IOException {
        directExchange();
    }

    //发送TTL消息
    private static void expireProducer() throws IOException {
        Channel channel = MQConnectionUtils.createChannel();
        //创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            //需要发送到队列中的消息
            String message = scanner.next();
            //设置消息的过期时间,消息过期后会入死信队列
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().expiration("1000").build();
            channel.basicPublish(EXCHANGE_NAME, "log", properties, message.getBytes());
        }
    }

    //direct交换机
    private static void directExchange() throws IOException {
        Channel channel = MQConnectionUtils.createChannel();
        //创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            //需要发送到队列中的消息
            String message = scanner.next();
            if (Integer.valueOf(message) % 2 == 0) {
                channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes());
            } else {
                channel.basicPublish(EXCHANGE_NAME, "erro", null, message.getBytes());
            }
        }
    }

    //fanout交换机
    private static void fanoutExchange() throws IOException {
        Channel channel = MQConnectionUtils.createChannel();
        //创建交换机
        channel.exchangeDeclare(
                EXCHANGE_NAME    //交换机名称
                , "fanout"       //交换机类型
        );
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            //需要发送到队列中的消息
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        }
    }

    //topic交换机
    private static void topicExchange() throws IOException {
        Channel channel = MQConnectionUtils.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            //需要发送到队列中的消息
            String message = scanner.next();
            if (Integer.valueOf(message) % 2 == 0) {
                channel.basicPublish(EXCHANGE_NAME, "xlw.info", null, message.getBytes());
            } else {
                channel.basicPublish(EXCHANGE_NAME, "klk.erro", null, message.getBytes());
            }
        }
    }
}
