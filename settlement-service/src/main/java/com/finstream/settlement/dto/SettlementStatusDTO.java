package com.finstream.settlement.dto;

import com.finstream.settlement.enums.SettlementState;
import lombok.Data;

import java.time.Instant;

@Data
public class SettlementStatusDTO {
    private String tradeId;
    private SettlementState state;
    private int retryCount;
    private Instant updatedAt;
}
