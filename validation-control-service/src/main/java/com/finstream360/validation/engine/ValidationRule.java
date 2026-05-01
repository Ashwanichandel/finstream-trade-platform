package com.finstream360.validation.engine;





import com.finstream360.validation.entity.Trade;
import com.finstream360.validation.entity.ValidationError;

import java.util.Optional;

public interface ValidationRule {

    Optional<ValidationError> validate(Trade trade, RuleExecutionContext context);

    int order();
}
