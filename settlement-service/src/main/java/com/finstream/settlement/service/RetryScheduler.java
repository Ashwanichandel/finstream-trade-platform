package com.finstream.settlement.service;


import com.finstream.settlement.dto.TradeDTO;
import com.finstream.settlement.entity.SettlementEntity;
import com.finstream.settlement.repository.SettlementRepository;
import com.finstream.settlement.repository.SettlementRetryExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Simple scheduler that periodically scans FAILED settlements and re-invokes attempt.
 * In production: use Kafka retry topic and backoff or a distributed scheduler.
 */
@Service
public class RetryScheduler {
    private final Logger log = LoggerFactory.getLogger(RetryScheduler.class);
    private final SettlementRepository repo;
    private final SettlementProcessor processor;
    private final SettlementRetryExecutor executor;

    @Value("${settlement.retry.backoff-ms:1500}")
    private long backoffMs;

    public RetryScheduler(SettlementRepository repo, SettlementProcessor processor, SettlementRetryExecutor executor) {
        this.repo = repo;
        this.processor = processor;
        this.executor = executor;
    }

 /*   public void scheduleRetry(String tradeId) {
        log.info("Scheduling retry for trade {}", tradeId);
        // no-op since scheduler scans DB and retries
    }*/
    public void scheduleRetry(String tradeId) {
        // @Scheduled / TaskScheduler / Quartz
        executor.retry(tradeId);
    }
    @Scheduled(fixedDelayString = "${settlement.retry.backoff-ms:1500}")
    public void pollAndRetry() {
        List<SettlementEntity> failed = repo.findAll().stream()
                .filter(s -> s.getState() != null && s.getState().name().equals("FAILED"))
                .toList();
        for (SettlementEntity s : failed) {
            // compose a TradeDTO minimal to retry (in prod, restore full context)
            TradeDTO t = new TradeDTO();
            t.setTradeId(s.getTradeId());
            t.setAccountId(s.getAccountId());
            t.setSecurityId(s.getSecurityId());
            t.setQuantity(s.getQuantity() > 0 ? s.getQuantity() : 0);
            t.setPrice(s.getAmount()!= null ? s.getAmount(): BigDecimal.ZERO);
            t.setCurrency("USD");
            t.setTradeDate(LocalDate.now());
            try {
                processor.attemptSettlement(s, t);
            } catch (Exception ex) {
                log.error("Retry attempt error for {}: {}", s.getTradeId(), ex.getMessage());
            }
        }
    }
}
