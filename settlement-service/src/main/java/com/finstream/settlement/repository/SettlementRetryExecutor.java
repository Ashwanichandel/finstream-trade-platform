package com.finstream.settlement.repository;

public interface SettlementRetryExecutor {
    void retry(String tradeId);
}
