package com.finstream360.validation.engine;



import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RuleExecutionContext {

    private final LocalDate processingDate = LocalDate.now();
}
