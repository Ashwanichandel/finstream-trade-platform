package com.finstream.settlement.service;

import com.finstream.settlement.enums.EventType;
import com.finstream.settlement.entity.SettlementEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * In production, this would publish to Kafka. For demo we log.
 */
@Service
public class ReconciliationPublisher {
    private final Logger log = LoggerFactory.getLogger(ReconciliationPublisher.class);

    public void publish(SettlementEntity s, EventType event) {
        log.info("RECONCILE-EVENT: trade={} event={} qty={} amount={}", s.getTradeId(), event, s.getQuantity(), s.getAmount());
    }
}
