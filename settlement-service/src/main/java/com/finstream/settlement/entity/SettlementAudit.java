package com.finstream.settlement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Data
@Table(name = "settlement_audit")
public class SettlementAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tradeId;
    private String action;
    private String details;
    private Instant timestamp = Instant.now();
    private String performedBy = "SYSTEM";
}
