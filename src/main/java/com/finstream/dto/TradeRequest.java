package com.finstream.dto;


import com.finstream.entity.TradeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TradeRequest {

    @NotBlank
    private String tradeReference;

    @NotBlank
    private String accountId;

    @NotBlank
    private String securityId;

    @NotBlank
    private String orderType;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    @Positive
    private Integer quantity;
    private Double tradeValue;
    private TradeStatus status;

}
