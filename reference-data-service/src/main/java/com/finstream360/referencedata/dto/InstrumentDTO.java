package com.finstream360.referencedata.dto;

import jakarta.validation.constraints.NotBlank;

public class InstrumentDTO {
    private Long id;
    @NotBlank
    private String isin;
    private String symbol;
    private String name;
    private String instrumentType;
    private String exchange;
    private String metadata; // JSON string
}
