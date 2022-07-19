package testspringboot.testspringboot.rabbitmq;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 当生产者发送消息到mq的交换机的时候,交换机就会发送一条确认消息给生产者,如果交换机找不到生产者发布消息的路由,那么会直接丢失该消息
 * 这是我们就得使用消息回退
 *
 * @author 肖龙威
 * @date 2022/07/18 16:18
 */
@Component
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitmqProperties properties;

    /**
     * 发送延迟消息
     *
     * @param message
     */
    public void send(String message, Integer delay) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString() + "xlw");
        rabbitTemplate.convertAndSend(
                //交换机名称
                properties.getExchangeName()
                //路由
                , properties.getRoutingKey()
                //消息
                , message
                //设置消息的属性
                , (properties) -> {
                    //设置延迟发送,必须配合延迟队列插件
                    //properties.getMessageProperties().setDelay(delay);
                    //设置优先级
                    properties.getMessageProperties().setPriority(5);
                    return properties;
                }
                //相关的数据,相当于设置数据的元数据
                , correlationData);
    }

}
