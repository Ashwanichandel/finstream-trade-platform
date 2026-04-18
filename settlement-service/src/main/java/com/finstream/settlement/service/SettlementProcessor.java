package com.finstream.settlement.service;

import com.finstream.settlement.dto.TradeDTO;
import com.finstream.settlement.entity.SettlementEntity;
import com.finstream.settlement.enums.EventType;
import com.finstream.settlement.enums.SettlementState;
import com.finstream.settlement.repository.SettlementRepository;
import com.finstream.settlement.repository.SettlementRetryExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

/**
 * SettlementProcessor contains domain logic for processing settlements.
 * It implements SettlementRetryExecutor so RetryScheduler can call retrySettlement(...)
 * without depending on the concrete processor class (breaks circular dependency).
 */
@Service
public class SettlementProcessor implements SettlementRetryExecutor {

    private static final Logger log = LoggerFactory.getLogger(SettlementProcessor.class);

    private final ReservationService reservationService;
    private final RiskValidator riskValidator;
    private final FxService fxService;
    private final LedgerClient ledgerClient;
    private final SettlementRepository repo;
    private final AuditService audit;
    private final ReconciliationPublisher recon;
    private final TradeQueryService tradeQueryService; // service to fetch trade details for retries

    @Value("${settlement.retry.max-attempts:3}")
    private int maxAttempts;

    public SettlementProcessor(
            ReservationService reservationService,
            RiskValidator riskValidator,
            FxService fxService,
            LedgerClient ledgerClient,
            SettlementRepository repo,
            AuditService audit,
            ReconciliationPublisher recon,
            TradeQueryService tradeQueryService
    ) {
        this.reservationService = reservationService;
        this.riskValidator = riskValidator;
        this.fxService = fxService;
        this.ledgerClient = ledgerClient;
        this.repo = repo;
        this.audit = audit;
        this.recon = recon;
        this.tradeQueryService = tradeQueryService;
    }

    /**
     * Idempotent entry point for settlement creation/processing.
     */
    public synchronized SettlementEntity processTrade(TradeDTO trade) {
        Optional<SettlementEntity> existing = repo.findByTradeId(trade.getTradeId());
        if (existing.isPresent()) {
            return existing.get();
        }

        SettlementEntity s = new SettlementEntity();
        s.setTradeId(trade.getTradeId());
        s.setAccountId(trade.getAccountId());
        s.setSecurityId(trade.getSecurityId());
        s.setQuantity(trade.getQuantity());
        s.setAmount(trade.getAmount());
        s.setState(trade.getState() != null ? trade.getState() : SettlementState.INITIATED);
        s.setRetryCount(trade.getRetryCount() != null ? trade.getRetryCount() : 0);

        s.setSettlementDate(Instant.now());
        s.setCreatedAt(Instant.now());
        s.setUpdatedAt(Instant.now());
        s = repo.save(s);

        audit.record(s.getTradeId(), "CREATED", "Settlement created");

        // schedule immediate attempt
        attemptSettlement(s, trade);
        return s;
    }

    /**
     * Core attempt logic (transactional).
     */
    @Transactional
    public void attemptSettlement(SettlementEntity s, TradeDTO trade) {
        log.info("Attempting settlement for trade={} attempt={}", s.getTradeId(), s.getRetryCount() + 1);

        try {
            // Step 1: Schedule
            s.setState(SettlementState.SCHEDULED);
            s.setUpdatedAt(Instant.now());
            repo.save(s);
            audit.record(s.getTradeId(), "SCHEDULED", "Settlement scheduled");

            // Step 2: Compute settlement date (T+2 demo)
            LocalDate settlementDate = computeSettlementDate(trade);
            log.info("Settlement date for trade {} is {}", s.getTradeId(), settlementDate);

            // Step 3: Reserve cash (FX applied)
            BigDecimal requiredAmount = s.getAmount();
            BigDecimal reservedAmount = fxService.convert(trade.getCurrency(), "USD", requiredAmount);

            reservationService.reserveCash(trade.getAccountId(), reservedAmount);
            s.setState(SettlementState.RESERVED);
            s.setUpdatedAt(Instant.now());
            repo.save(s);
            audit.record(s.getTradeId(), "RESERVED", "Cash reserved: " + reservedAmount);

            // Step 4: Reserve securities
            int reservedQty = reservationService.reserveSecurities(trade.getAccountId(), trade.getSecurityId(), s.getQuantity());
            if (reservedQty <= 0) {
                throw new RuntimeException("No securities available for reservation");
            }

            // Step 5: Risk validation
            riskValidator.validate(trade.getAccountId(), trade.getSecurityId(), reservedQty);
            s.setState(SettlementState.RISK_APPROVED);
            s.setUpdatedAt(Instant.now());
            repo.save(s);
            audit.record(s.getTradeId(), "RISK_APPROVED", "Risk OK for qty " + reservedQty);

            // Step 6: Partial vs Full Settlement
            if (reservedQty < s.getQuantity()) {
                // Partial
                BigDecimal partialAmount = reservedAmount.multiply(BigDecimal.valueOf(reservedQty))
                        .divide(BigDecimal.valueOf(s.getQuantity()));
                executeDvP(s, reservedQty, partialAmount);

                s.setQuantity(s.getQuantity() - reservedQty);
                s.setState(SettlementState.PARTIALLY_SETTLED);
                s.setUpdatedAt(Instant.now());
                repo.save(s);

                audit.record(s.getTradeId(), "PARTIAL", "Partially settled qty " + reservedQty);
                recon.publish(s, EventType.SETTLEMENT_PARTIAL);

            } else {
                // Full
                executeDvP(s, s.getQuantity(), reservedAmount);

                s.setQuantity(0);
                s.setState(SettlementState.SETTLED);
                s.setUpdatedAt(Instant.now());
                repo.save(s);

                audit.record(s.getTradeId(), "SETTLED", "Fully settled");
                recon.publish(s, EventType.SETTLEMENT_COMPLETED);
            }
        } catch (Exception ex) {
            log.error("Settlement attempt failed for trade {}: {}", s.getTradeId(), ex.getMessage(), ex);
            handleFail(s, ex.getMessage());
        }
    }

    private LocalDate computeSettlementDate(TradeDTO trade) {
        return trade.getTradeDate().plusDays("EQUITY".equalsIgnoreCase(trade.getInstrumentType()) ? 2 : 0);
    }

    private void executeDvP(SettlementEntity s, int qty, BigDecimal amount) {
        ledgerClient.postDebit(s.getAccountId(), amount, "TRADE_SETTLEMENT_DEBIT:" + s.getTradeId());
        ledgerClient.postCredit(s.getAccountId(), amount, "TRADE_SETTLEMENT_CREDIT:" + s.getTradeId());
    }

    private void handleFail(SettlementEntity s, String reason) {
        s.setRetryCount(s.getRetryCount() + 1);
        s.setState(SettlementState.FAILED);
        s.setUpdatedAt(Instant.now());
        repo.save(s);

        audit.record(s.getTradeId(), "FAILED", reason);

        if (s.getRetryCount() < maxAttempts) {
            // Instead of calling a scheduler directly, emit an event or call an infra component.
            // Here we only record the need for retry; external scheduler/infra should poll or listen.
            // If you want scheduler to schedule a retry immediately, use RetryScheduler that depends on SettlementRetryExecutor.
            // Example (in RetryScheduler): executor.retrySettlement(tradeId)
        } else {
            recon.publish(s, EventType.SETTLEMENT_FAILED);
            audit.record(s.getTradeId(), "ESCALATED", "Max retry attempts reached");
        }
    }

    /**
     * Called by RetryScheduler (via the SettlementRetryExecutor abstraction)
     */


    @Override
    public void retry(String tradeId) {
        SettlementEntity s = repo.findByTradeId(tradeId).orElseThrow(() -> new IllegalStateException("Settlement not found: " + tradeId));
        // Fetch latest TradeDTO (implementation detail - create TradeQueryService to provide this)
        TradeDTO trade = tradeQueryService.fetchTrade(tradeId); // <-- ensure TradeQueryService exists and is injected
        attemptSettlement(s, trade);
    }
}
