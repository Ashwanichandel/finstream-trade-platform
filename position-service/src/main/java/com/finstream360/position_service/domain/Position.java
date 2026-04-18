package com.finstream360.position_service.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.kafka.common.annotation.InterfaceStability;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "position")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;
    private String instrumentId;

    @Enumerated(EnumType.STRING)
    private PositionType type;

    private BigDecimal quantity;
    private BigDecimal blockedQuantity;

    @Enumerated(EnumType.STRING)
    private PositionStatus status;

    private LocalDateTime updatedAt;

}
