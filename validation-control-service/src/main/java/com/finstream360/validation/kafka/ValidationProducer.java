package com.finstream360.validation.kafka;

import com.finstream360.validation.entity.ValidationResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ValidationProducer {

    private final KafkaTemplate<String, ValidationResult> kafkaTemplate;

    public ValidationProducer(KafkaTemplate<String, ValidationResult> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(ValidationResult result) {
        kafkaTemplate.send("validated-trades", result.getTradeId(), result);
    }
}
