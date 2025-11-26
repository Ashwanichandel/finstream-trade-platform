package com.finstream.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeId;

    @Column(nullable = false, unique = true)
    private String tradeReference;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String securityId;

    @Column(nullable = false)
    private String orderType;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    private Double tradeValue;

    private LocalDateTime tradeTimestamp;
    private LocalDateTime createdAt;
}
