package com.finstream.settlement.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory reservation store for demo. In prod, use ledger/position service.
 */
@Service
public class ReservationService {
    private final ConcurrentHashMap<String, BigDecimal> cashHolds = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> securityHolds = new ConcurrentHashMap<>();

    public void reserveCash(String accountId, BigDecimal amount) {
        // demo: assume each account has 1_000_000 balance
        BigDecimal balance = BigDecimal.valueOf(1_000_000);
        BigDecimal held = cashHolds.getOrDefault(accountId, BigDecimal.ZERO);
        if (balance.subtract(held).compareTo(amount) < 0) {
            throw new com.finstream.settlement.exception.InsufficientFundsException("Insufficient cash for reserve");
        }
        cashHolds.put(accountId, held.add(amount));
    }

    public int reserveSecurities(String accountId, String securityId, int qty) {
        // demo: assume each account has 1000 of each security
        int available = 1000 - securityHolds.getOrDefault(accountId + ":" + securityId, 0);
        int toReserve = Math.min(available, qty);
        if (toReserve <= 0) return 0;
        securityHolds.put(accountId + ":" + securityId, securityHolds.getOrDefault(accountId + ":" + securityId, 0) + toReserve);
        return toReserve;
    }

    public void releaseCash(String accountId, java.math.BigDecimal amount) {
        BigDecimal held = cashHolds.getOrDefault(accountId, BigDecimal.ZERO);
        cashHolds.put(accountId, held.subtract(amount).max(BigDecimal.ZERO));
    }

    public void releaseSecurities(String accountId, String securityId, int qty) {
        String key = accountId + ":" + securityId;
        int held = securityHolds.getOrDefault(key, 0);
        securityHolds.put(key, Math.max(0, held - qty));
    }
}
