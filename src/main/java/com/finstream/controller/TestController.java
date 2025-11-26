package com.finstream.controller;

import com.finstream.entity.Trade;
import com.finstream.service.KafkaEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.finstream.entity.TradeStatus.NEW;

@RestController
@RequestMapping("/test")
public class TestController {

    private final KafkaEventPublisher kafkaEventPublisher;

    public TestController(KafkaEventPublisher kafkaEventPublisher) {
        this.kafkaEventPublisher = kafkaEventPublisher;
    }

    @PostMapping("/publish")
    public String publishTrade() {
        Trade trade = Trade.builder()
                .tradeReference("TRD-TEST-001")
                .accountId("ACC-123")
                .price(100.0)
                .quantity(10)
                .securityId("SEC-001")
                .status(NEW)
                .build();

        kafkaEventPublisher.publishTradeEvent(trade);
        return "Trade event published!";
    }
}
