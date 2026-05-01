package com.finstream.settlement.service;

import org.springframework.stereotype.Service;

/**
 * Simple risk check: ensure qty <= limit (demo)
 */
@Service
public class RiskValidator {
    public void validate(String accountId, String securityId, int qty) {
        int limit = 5000; // demo
        if (qty > limit) {
            throw new com.finstream.settlement.exception.RiskBreachException("quantity exceeds risk limit");
        }
    }
}
