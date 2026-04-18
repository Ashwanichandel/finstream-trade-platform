package com.finstream.settlement.entity;

import com.finstream.settlement.enums.SettlementState;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@Table(name = "settlements")
public class SettlementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "tradeid")
    private String tradeId;


    private String accountId;
    private String securityId;
    private int quantity;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private SettlementState state;

    private Integer retryCount;

    private Instant settlementDate;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
}
