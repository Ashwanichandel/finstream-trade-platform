package com.finstream.service;


import com.finstream.entity.Trade;

import java.util.List;

public interface KafkaEventPublisher {
    void publishTradeEvent(Trade trade);
    void publishTradeEvents(List<Trade> trades);
}

