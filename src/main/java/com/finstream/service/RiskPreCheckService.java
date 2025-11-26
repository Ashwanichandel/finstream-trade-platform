package com.finstream.service;

import com.finstream.dto.TradeRequest;

public interface RiskPreCheckService {
    void validate(TradeRequest request);
}

