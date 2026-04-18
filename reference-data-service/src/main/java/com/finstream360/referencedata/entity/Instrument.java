package com.finstream360.referencedata.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Entity
@Table(name = "instruments")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Instrument {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true)
    private String isin;


    private String symbol;
    private String name;
    private String instrumentType;
    private String exchange;


    @Column(columnDefinition = "jsonb")
    private String metadata;


    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;


    @PrePersist
    public void prePersist() { createdAt = OffsetDateTime.now(); }
    @PreUpdate
    public void preUpdate() { updatedAt = OffsetDateTime.now(); }
}