package com.finstream.settlement.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finstream.settlement.dto.TradeDTO;
import com.finstream.settlement.service.SettlementProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SettlementConsumer {
    private final Logger log = LoggerFactory.getLogger(SettlementConsumer.class);
    private final SettlementProcessor processor;
    private final ObjectMapper mapper = new ObjectMapper();

    public SettlementConsumer(SettlementProcessor processor) {
        this.processor = processor;
    }

    @KafkaListener(topics = "${kafka.topics.incoming}", groupId = "settlement-group")
    public void onMessage(String message) {
        try {
            TradeDTO trade = mapper.readValue(message, TradeDTO.class);
            processor.processTrade(trade);
        } catch (Exception ex) {
            log.error("Kafka processing error: {}", ex.getMessage());
            // in production: send to DLQ
        }
    }
}
