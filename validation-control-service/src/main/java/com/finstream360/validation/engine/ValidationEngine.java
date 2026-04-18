package com.finstream360.validation.engine;

import com.finstream360.validation.entity.Severity;
import com.finstream360.validation.entity.Trade;
import com.finstream360.validation.entity.TradeStatus;
import com.finstream360.validation.entity.ValidationResult;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ValidationEngine {
    private final List<ValidationRule> rules;

    public ValidationEngine(List<ValidationRule> rules) {
        this.rules = rules.stream()
                .sorted(Comparator
                        .comparingInt(ValidationRule::order))
                .toList();
    }

    public ValidationResult validate(Trade trade) {
        ValidationResult result = new ValidationResult();
        result.setTradeId(trade.getTradeId());

        RuleExecutionContext context = new RuleExecutionContext();
        rules.forEach(rule -> rule.validate(trade, context)
                .ifPresent(result.getErrors()::add));
        boolean hasError = result.getErrors().stream()
                .anyMatch(e -> e.getSeverity() == Severity.ERROR);

        boolean hasWarning = result.getErrors().stream()
                .anyMatch(e -> e.getSeverity() == Severity.WARNING);

        if (hasError) {
            result.setStatus(TradeStatus.REJECTED);
        } else if (hasWarning) {
            result.setStatus(TradeStatus.PENDING_REVIEW);
        } else {
            result.setStatus(TradeStatus.VALIDATED);
        }

        return result;
    }
}
