package com.finstream.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "counterparty_limits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CounterpartyLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountId;
    private Double maxLimit;        // e.g. 1 crore exposure
    private Double usedLimit;       // how much exposure already used
}
