package com.finstream.service;

import com.finstream.dto.TradeRequest;
import com.finstream.exception.TradeException;
import org.springframework.stereotype.Service;

@Service
public class RiskPreCheckServiceImpl implements RiskPreCheckService {
    @Override
    public void validate(TradeRequest req) {
        if (!"BUY".equalsIgnoreCase(req.getOrderType()) && !"SELL".equalsIgnoreCase(req.getOrderType())) {
            throw new TradeException("Invalid order type: " + req.getOrderType());
        }
        if (req.getQuantity() <= 0 || req.getPrice() <= 0) {
            throw new TradeException("Price and quantity must be positive");
        }
    }
}
