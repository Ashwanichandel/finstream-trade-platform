package com.finstream360.clearing.engine.netting;

import com.finstream360.clearing.domain.Trade;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MultilateralNettingStrategy  implements NettingStrategy{

    @Override
    public Map<String, BigDecimal> net(List<Trade> trades) {
        return trades.stream().collect(
                Collectors.groupingBy(
                        Trade::getCounterparty,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Trade::tradeValue,
                                BigDecimal::add
                        )
                )
        );
    }
}
