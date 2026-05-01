package com.finstream360.clearing.engine;

import com.finstream360.clearing.domain.Trade;
import com.finstream360.clearing.domain.enums.NettingType;
import com.finstream360.clearing.engine.netting.NettingStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class ClearingEngine {

    private final NettingEngine nettingEngine;
    private final MarginEngine marginEngine;
    private final ExposureEngine exposureEngine;
    private final ClearingStateMachine stateMachine;

    public ClearingEngine(NettingEngine nettingEngine,
                          MarginEngine marginEngine,
                          ExposureEngine exposureEngine,
                          ClearingStateMachine stateMachine) {
        this.nettingEngine = nettingEngine;
        this.marginEngine = marginEngine;
        this.exposureEngine = exposureEngine;
        this.stateMachine = stateMachine;
    }

    public void clear(List<Trade> trades) {

        Map<String, BigDecimal> netted =
                nettingEngine.execute(NettingType.MULTILATERAL, trades);

        netted.forEach((cp, exposure) -> {
            exposureEngine.updateExposure(cp, exposure);
            BigDecimal margin = marginEngine.calculateInitialMargin(exposure);

            if (margin.compareTo(new BigDecimal("1000000")) > 0) {
                throw new ExposureLimitBreachException(cp);
            }
        });

        trades.forEach(t ->
                t.setStatus(stateMachine.next(t.getStatus())));
    }
}


