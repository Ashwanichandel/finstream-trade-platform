package com.finstream.service;

import com.finstream.entity.Trade;
import com.finstream.event.TradeEvent;
import com.finstream.event.TradeEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service

public class KafkaEventPublisherImpl implements KafkaEventPublisher {

    private final KafkaTemplate<String, TradeEvent> kafkaTemplate;

    @Value("${kafka.topic.trade-events}")
    private String topic;
    public KafkaEventPublisherImpl(KafkaTemplate<String, TradeEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishTradeEvent(Trade trade) {

        TradeEvent event = TradeEvent.builder()
                .tradeReference(trade.getTradeReference())
                .eventType(TradeEventType.TRADE_CAPTURED)
                .tradeValue(trade.getTradeValue())
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(topic, trade.getTradeReference(), event);

        log.info("Published trade event for tradeReference={}", trade.getTradeReference());
    }
    @Override
    public void publishTradeEvents(List<Trade> trades) {
        for (Trade trade : trades) {
            publishTradeEvent(trade); // reuse single-trade method
        }
    }

}
