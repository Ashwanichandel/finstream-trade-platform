package com.finstream360.clearing.engine.netting;

import com.finstream360.clearing.domain.Trade;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface NettingStrategy {
    Map<String, BigDecimal> net(List<Trade> trades);
}
