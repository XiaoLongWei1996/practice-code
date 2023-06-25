package com.xlw.kafka_demo.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @className: KafkaConfig
 * @author: xlw
 * @date: 2023/6/7 16:53
 **/
@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic createTopic() {
        return new NewTopic("topic-1", 4, (short) 1);
    }

}
