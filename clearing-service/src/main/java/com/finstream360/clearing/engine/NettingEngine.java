package com.finstream360.clearing.engine;

import com.finstream360.clearing.domain.Trade;
import com.finstream360.clearing.domain.enums.NettingType;
import com.finstream360.clearing.engine.netting.MultilateralNettingStrategy;
import com.finstream360.clearing.engine.netting.NettingStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class NettingEngine {

    private final Map<NettingType, NettingStrategy> strategyMap;

    public NettingEngine(List<NettingStrategy> strategies) {
        this.strategyMap = new EnumMap<>(NettingType.class);
        strategies.forEach(s -> {
            if (s instanceof MultilateralNettingStrategy)
                strategyMap.put(NettingType.MULTILATERAL, s);
        });
    }

    public Map<String, BigDecimal> execute(NettingType type, List<Trade> trades) {
        return strategyMap.get(type).net(trades);
    }
}

