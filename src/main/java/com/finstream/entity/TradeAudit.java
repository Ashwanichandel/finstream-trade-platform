package com.finstream.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trade_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tradeReference;
    private String action;          // CREATED, UPDATED, LIMIT_FAILED
    private LocalDateTime timestamp;
    private String performedBy;
}
