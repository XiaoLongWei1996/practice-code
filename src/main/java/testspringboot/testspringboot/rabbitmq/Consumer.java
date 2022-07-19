package testspringboot.testspringboot.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 肖龙威
 * @date 2022/07/18 16:27
 */
@Component
public class Consumer {


    @RabbitListener(queues = "qu-delayed")
    public void received(Message message, Channel channel){
        System.out.println("消费:" + new String(message.getBody()));
    }

    @RabbitListener(queues = "qu-back")
    public void backReceived(Message message, Channel channel) {
        System.out.println("备份队列消费:" + new String(message.getBody()));
    }

}
