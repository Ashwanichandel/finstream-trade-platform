package com.finstream360.validation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "trade")
@Data
public class Trade {
    @Id
    private String tradeId;
    private String instrumentId;
    private String counterpartyId;
    private LocalDate tradeDate;
    private LocalDate settlementDate;
    private BigDecimal quantity;
    private BigDecimal price;
    private String currency;
    private BigDecimal riskScore;
}
