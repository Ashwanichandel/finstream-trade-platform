package com.finstream360.validation.rules;



import com.finstream360.validation.engine.RuleExecutionContext;
import com.finstream360.validation.engine.ValidationRule;
import com.finstream360.validation.entity.Severity;
import com.finstream360.validation.entity.Trade;
import com.finstream360.validation.entity.ValidationError;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TradeDateRule implements ValidationRule {

    @Override
    public Optional<ValidationError> validate(Trade trade, RuleExecutionContext context) {
        if (trade.getTradeDate().isAfter(context.getProcessingDate())) {
            return Optional.of(new ValidationError(
                    "TradeDateRule",
                    "Trade date cannot be in the future",
                    Severity.ERROR
            ));
        }
        return Optional.empty();
    }



    @Override
    public int order() {
        return 1;
    }
}
