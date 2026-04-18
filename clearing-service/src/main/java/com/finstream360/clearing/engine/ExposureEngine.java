package com.finstream360.clearing.engine;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExposureEngine {

    private final ConcurrentHashMap<String, BigDecimal> exposureMap =
            new ConcurrentHashMap<>();

    public void updateExposure(String counterparty, BigDecimal value) {
        exposureMap.merge(counterparty, value, BigDecimal::add);
    }

    public BigDecimal getExposure(String counterparty) {
        return exposureMap.getOrDefault(counterparty, BigDecimal.ZERO);
    }
}
