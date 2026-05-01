package com.finstream.settlement.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic settlementEvents() {
        return new NewTopic("settlement.events", 1, (short)1);
    }

    @Bean
    public NewTopic settlementRetry() {
        return new NewTopic("settlement.retry", 1, (short)1);
    }
}
