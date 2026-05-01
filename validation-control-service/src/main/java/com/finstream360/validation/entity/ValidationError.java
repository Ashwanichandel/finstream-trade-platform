package com.finstream360.validation.entity;

import lombok.Data;

@Data
public class ValidationError {
    private String rule;
    private String message;
    private Severity severity;

    public ValidationError(String tradeDateRule, String s, Severity severity) {
    }
}
