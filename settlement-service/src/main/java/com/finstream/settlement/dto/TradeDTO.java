package com.finstream.settlement.dto;

import com.finstream.settlement.enums.SettlementState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TradeDTO {
    @NotBlank
    private String tradeId;
    @NotBlank
    private String accountId;
    @NotBlank
    private String securityId;
    @NotNull
    private Integer quantity;
    @NotNull
    private BigDecimal price;
    @NotBlank
    private String currency;
    @NotNull
    private LocalDate tradeDate;
    private Integer retryCount;
    private BigDecimal amount;
    private String instrumentType = "EQUITY"; // default
    private boolean validated = true;
    private boolean enriched = true;
    private SettlementState state;
}