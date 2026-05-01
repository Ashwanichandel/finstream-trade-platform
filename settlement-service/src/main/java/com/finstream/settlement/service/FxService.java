package com.finstream.settlement.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FxService {
    public BigDecimal convert(String from, String to, BigDecimal amount) {
        // in prod: call FX rates & apply spread
        return amount;
    }
}