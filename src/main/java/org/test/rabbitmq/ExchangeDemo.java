package org.test.rabbitmq;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;

/**
 * Exchange(交换机):生产者把消息发送给mq不是直接添加到mq的队列中，而是由交换机接收到信息，再由交换机通过routingKey决定是发送到那个队列，还是
 *                 发送给多个队列。
 * Bindings(绑定):将交换机与那个或者那几个队列进行绑定
 * @author 肖龙威
 * @date 2022/07/13 15:37
 */
public class ExchangeDemo {

    private static final String EXCHANGE_NAME = "log";

    @SuppressWarnings("all")
    public static void main(String[] args) throws IOException {
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
            channel.basicPublish(EXCHANGE_NAME,"", null, message.getBytes());
        }
    }
}
