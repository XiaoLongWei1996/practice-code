package testspringboot.testspringboot.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 肖龙威
 * @date 2022/07/18 14:58
 */
@Configuration
/**
 * 把RabbitmqProperties加入到容器
 */
@EnableConfigurationProperties({RabbitmqProperties.class})
public class RabbitmqConfig {

    private RabbitmqProperties properties;

    private RabbitTemplate rabbitTemplate;

    public RabbitmqConfig(RabbitmqProperties properties, RabbitTemplate rabbitTemplate) {
        this.properties = properties;
        this.rabbitTemplate = rabbitTemplate;
    }

    //初始化方法
    @PostConstruct
    public void initRabbitTemplate(){
        System.out.println("初始化");
        //设置发布回调
        rabbitTemplate.setConfirmCallback((
                correlationData //消息的相关数据
                , confirm   //mq交换机是否接收到消息
                , cause     //交换机消息接收失败的原因
        ) -> {
            if(confirm) {
                System.out.println("成功收到:" + correlationData.getId() + "消息");
            } else {
                System.out.println("接收消息:" + correlationData.getId() + "失败,原因=====" + cause);
            }
        });

        //设置消息回退,同时启用消息退回和备份交换机,会优先使用备份交换机
        //true交换机无法找到路由相关的队列时,会把消息返回给生产者
        //false交换机无法找到路由相关的队列时,会直接丢失该消息
        rabbitTemplate.setMandatory(true);
        //设置回退消息处理回调函数
        rabbitTemplate.setReturnsCallback((returnedMessage) -> {
            System.out.println("消息:" + new String(returnedMessage.getMessage().getBody()) + "被路由:" + returnedMessage.getExchange() + "退回" + "原因:" + returnedMessage.getReplyText());
        });
    }

    /**
     * 创建队列
     * @return
     */
    @Bean
    public Queue delayedQueue() {
        Map<String, Object> property = new HashMap<>();
        //设置队列的优先级,该队列可以优先被消费
        property.put("x-max-priority", 10);
        //设置惰性队列,惰性队列会尽可能的将消息存入磁盘中，而在消费者消费到相应的消息时才会被加载到内存中,节省内存资源
        property.put("x-queue-mode", "lazy");
        return new Queue(properties.getQueueName(), false, false, false, property);
    }

//    /**
//     * 创建延迟交换机
//     * @return
//     */
//    @Bean
//    public CustomExchange customExchange() {
//        Map<String, Object> property = new HashMap<>();
//        property.put("x-delayed-type", "direct");
//        return new CustomExchange(properties.getExchangeName(), "x-delayed-message", false, false, property);
//    }

    @Bean
    public DirectExchange directExchange() {
        Map<String, Object> property = new HashMap<>();
        //设置备份交换机
        property.put("alternate-exchange",properties.getBackExchangeName());
        return new DirectExchange(properties.getExchangeName(), false, false, property);
    }

    /**
     * 创建队列跟交换机的绑定对象
     * @param delayedQueue
     * @param
     * @return
     */
    @Bean
    public Binding binding(Queue delayedQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(delayedQueue).to(directExchange).with(properties.getRoutingKey());
    }

    /**
     * 备份交换机,跟普通交换机进行关联,普通交换机退回的消息会转发给备份交换机
     * 同时启用消息退回和备份交换机,会优先使用备份交换机
     * @return
     */
    @Bean
    public FanoutExchange backExchange() {
        return new FanoutExchange(properties.getBackExchangeName(), false, false);
    }

    /**
     * 备份队列,收集退回的消息
     * @return
     */
    @Bean
    public Queue backQueue() {
        return new Queue(properties.getBackQueueName(), false, false, false, null);
    }

    @Bean
    public Binding backBinding(Queue backQueue, FanoutExchange backExchange) {
        return BindingBuilder.bind(backQueue).to(backExchange);
    }

}
