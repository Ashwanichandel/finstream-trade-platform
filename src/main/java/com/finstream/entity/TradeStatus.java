package com.finstream.entity;

public enum TradeStatus {
    NEW,
    VALIDATED,
    LIMIT_CHECK_FAILED,
    RISK_CHECK_FAILED,
    SENT_TO_RISK,
    ACCEPTED,
    REJECTED
}
