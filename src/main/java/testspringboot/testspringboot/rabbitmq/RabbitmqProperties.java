package testspringboot.testspringboot.rabbitmq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 肖龙威
 * @date 2022/07/18 15:00
 */
@ConfigurationProperties(prefix = "rmq")
@Data
public class RabbitmqProperties {

    private String exchangeName;

    private String queueName;

    private String routingKey;

    private String backExchangeName;

    private String backQueueName;
}
