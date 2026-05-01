package com.finstream360.clearing.engine;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MarginEngine {

    public BigDecimal calculateInitialMargin(BigDecimal exposure) {
        return exposure.multiply(new BigDecimal("0.15"));
    }

    public BigDecimal calculateVariationMargin(BigDecimal pnl) {
        return pnl.abs().multiply(new BigDecimal("0.10"));
    }
}
