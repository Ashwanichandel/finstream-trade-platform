package com.finstream.settlement.service;

import com.finstream.settlement.dto.TradeDTO;
import org.springframework.stereotype.Service;

@Service
public class TradeQueryService {
    public TradeDTO fetchTrade(String tradeId) {
        // fetch from trade-service, or reconstruct from DB/audit
        throw new UnsupportedOperationException("Implement trade fetch logic");
    }
}
