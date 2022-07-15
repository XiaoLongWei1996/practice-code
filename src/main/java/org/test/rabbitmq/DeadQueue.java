package org.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列:当队列中的一些消息因为某些原因无法被消费掉,就会进入死信队列;
 * 导致进入死刑队列的原因:
 * 1.消息TTL过期;
 * 2.队列达到最大的长度,无法再添加数据;
 * 3.消息消费被拒绝(basic.reject 或 basic.nack)并且 requeue=false.
 *
 * @author 肖龙威
 * @date 2022/07/15 13:42
 */
public class DeadQueue {

    //一般交换机名称
    private static final String EXCHANGE_NAME = "log";

    private static final String QUEUE_NAME = "queue";

    //死信交换机名称
    private static final String DEAD_EXCHANGE_NAME = "dead_log";

    //死信队列名称
    private static final String DEAD_QUEUE_NAME = "dead_queue";

    @SuppressWarnings("all")
    public static void main(String[] args) throws IOException {
        Channel channel = MQConnectionUtils.createChannel();
        //创建死信队列交换机
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //创建普通交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //创建死信队列
        channel.queueDeclare(DEAD_QUEUE_NAME, false, false, false, null);
        //绑定死刑交换机
        channel.queueBind(DEAD_QUEUE_NAME, DEAD_EXCHANGE_NAME, "dead");

        //创建普通队列
        //将正常队列绑定死信队列
        Map<String, Object> params = new HashMap<>();
        //指定死信队列交换机
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        //指定死信队列路由
        params.put("x-dead-letter-routing-key", "dead");
        //指定队列最大长度,当队列里面的消息超过10条后的消息会入死信队列
        params.put("x-max-length", 10);
        channel.queueDeclare(QUEUE_NAME, false, false, false, params);
        //绑定交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "log");

        channel.basicConsume(QUEUE_NAME, false  //不自动签收
                , ((consumerTag, message) -> {
                    String s = new String(message.getBody());
                    System.out.println(s);
                    if (Integer.valueOf(s) % 2 == 0) {
                        channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                    } else {
                        //拒绝消费消息,requeue是否重新入队列,true重新入队列,false则是该消息丢弃/入死信队列
                        channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                    }
                }), consumerTag -> {});
    }
}
