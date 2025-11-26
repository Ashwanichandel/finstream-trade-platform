package com.finstream.service;


import com.finstream.dto.TradeRequest;
import com.finstream.dto.TradeResponse;
import com.finstream.entity.Trade;
import com.finstream.entity.TradeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface TradeService {
    TradeResponse captureTrade(TradeRequest request);

    List<TradeResponse> captureTrades(List<TradeRequest> request);

    TradeResponse getTradeById(Long tradeId);
    List<TradeResponse> getAllTrades(int page, int size, String accountId, String securityId, TradeStatus status);



    Map<String, Object> getTradeStatistics(String accountId);

    TradeResponse updateTrade(Long tradeId, TradeRequest request);

    void deleteTrade(Long tradeId);
}
