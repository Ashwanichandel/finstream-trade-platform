package com.finstream.service;

public interface LimitCheckService {
    void checkLimit(String accountId, Integer quantity, Double tradeValue);
}

