package com.finstream360.validation.controller;

import com.finstream360.validation.engine.ValidationEngine;
import com.finstream360.validation.entity.Trade;
import com.finstream360.validation.entity.ValidationResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/validation")
public class ValidationController {
    private final ValidationEngine engine;

    public ValidationController(ValidationEngine engine) {
        this.engine = engine;
    }

    @PostMapping
    public ValidationResult validateTrade(@RequestBody Trade trade){
        return engine.validate(trade);
    }
}
