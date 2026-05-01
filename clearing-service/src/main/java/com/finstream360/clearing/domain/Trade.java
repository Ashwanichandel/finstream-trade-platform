package com.finstream360.clearing.domain;

import com.finstream360.clearing.domain.enums.ClearingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class Trade {
    private String tradeId;
    private String counterparty;
    private String instrument;
    private BigDecimal quantity;
    private BigDecimal price;
    private LocalDate tradeDate;
    private ClearingStatus status;

    public BigDecimal tradeValue() {
        return quantity.multiply(price);
    }
}
