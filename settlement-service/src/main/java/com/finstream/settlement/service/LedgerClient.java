package com.finstream.settlement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Ledger stub: in prod, this posts to ledger service.
 */
@Service
public class LedgerClient {
    private final Logger log = LoggerFactory.getLogger(LedgerClient.class);

    public void postDebit(String accountId, BigDecimal amount, String reason) {
        log.info("Ledger debit: {} {} {}", accountId, amount, reason);
    }

    public void postCredit(String accountId, BigDecimal amount, String reason) {
        log.info("Ledger credit: {} {} {}", accountId, amount, reason);
    }
}
