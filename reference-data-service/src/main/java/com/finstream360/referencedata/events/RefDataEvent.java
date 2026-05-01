package com.finstream360.referencedata.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefDataEvent {
    private String entity; // instrument/counterparty/currency
    private String action; // CREATED/UPDATED/DELETED
    private Long id;
    private String payload; // JSON string
}
