package com.finstream.dto.mapper;

import com.finstream.dto.TradeRequest;
import com.finstream.dto.TradeResponse;
import com.finstream.entity.Trade;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TradeMapper {

    /*private final ModelMapper modelMapper;

    public TradeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        // IMPORTANT: Skip tradeId mapping
        this.modelMapper.typeMap(TradeRequest.class, Trade.class)
                .addMappings(mapper -> mapper.skip(Trade::setTradeId));
    }*/

    public Trade toEntity(TradeRequest request) {
        Trade trade = new Trade();
        trade.setTradeReference(request.getTradeReference());
        trade.setAccountId(request.getAccountId());
        trade.setSecurityId(request.getSecurityId());
        trade.setOrderType(request.getOrderType());
        trade.setPrice(request.getPrice());
        trade.setTradeValue(request.getTradeValue());
        trade.setQuantity(request.getQuantity());
        return trade;
    }

    public TradeResponse toResponse(Trade trade) {
        return TradeResponse.builder()
                .tradeId(trade.getTradeId())
                .tradeReference(trade.getTradeReference())
                .accountId(trade.getAccountId())
                .securityId(trade.getSecurityId())
                .orderType(trade.getOrderType())
                .price(trade.getPrice())
                .quantity(trade.getQuantity())
                .status(trade.getStatus())
                .tradeValue(trade.getTradeValue())
                .tradeTimestamp(trade.getTradeTimestamp())
                .build();
    }
}
