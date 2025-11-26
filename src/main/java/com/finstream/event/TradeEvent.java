package com.finstream.event;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TradeEvent {
    private String tradeReference;
    private TradeEventType eventType;
    private Double tradeValue;
    private LocalDateTime timestamp;
}
