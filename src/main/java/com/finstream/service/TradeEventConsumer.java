package com.finstream.service;


import com.finstream.event.TradeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TradeEventConsumer {

    @KafkaListener(topics = "${kafka.topic.trade-events}", groupId = "trade-event-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeTradeEvent(TradeEvent event) {
        log.info("Consumed trade event: tradeReference={}, eventType={}, tradeValue={}",
                event.getTradeReference(), event.getEventType(), event.getTradeValue());
    }
}
