package com.finstream360.validation.rules;


import com.finstream360.validation.engine.RuleExecutionContext;
import com.finstream360.validation.engine.ValidationRule;

import com.finstream360.validation.entity.Severity;
import com.finstream360.validation.entity.Trade;
import com.finstream360.validation.entity.ValidationError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class RiskThresholdRule implements ValidationRule {

    @Value("${rules.risk.max-score}")
    private BigDecimal maxRisk;

    @Override
    public Optional<ValidationError> validate(Trade trade, RuleExecutionContext context) {
        if (trade.getRiskScore().compareTo(maxRisk) > 0) {
            return Optional.of(new ValidationError(
                    "RiskThresholdRule",
                    "Risk score exceeds allowed threshold",
                    Severity.WARNING
            ));
        }
        return Optional.empty();
    }

    @Override
    public int order() {
        return 5;
    }
}
