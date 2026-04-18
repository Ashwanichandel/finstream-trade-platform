package com.finstream360.validation.entity;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ValidationResult {
    private String tradeId;
    private TradeStatus status;
    private List<ValidationError> errors = new ArrayList<>();
}
