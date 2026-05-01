package com.finstream360.validation.kafka;

import com.finstream360.validation.engine.ValidationEngine;
import com.finstream360.validation.entity.Trade;
import com.finstream360.validation.entity.ValidationResult;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TradeConsumer {

    private final ValidationEngine engine;
    private final ValidationProducer producer;

    public TradeConsumer(ValidationEngine engine, ValidationProducer producer) {
        this.engine = engine;
        this.producer = producer;
    }

    @KafkaListener(topics = "enriched-trades", groupId = "validation-group")
    public void consume(Trade trade) {
        ValidationResult result = engine.validate(trade);
        producer.publish(result);
    }
}
