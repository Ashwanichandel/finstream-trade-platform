package com.finstream.dto;


import com.finstream.entity.TradeStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TradeResponse {
    private Long tradeId;
    private String accountId;
    private String securityId;
    private String orderType;
    private Double price;
    private Integer quantity;
    private String tradeReference;
    private TradeStatus status;
    private Double tradeValue;
    private LocalDateTime tradeTimestamp;
}

