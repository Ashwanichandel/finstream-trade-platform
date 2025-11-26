package com.finstream.service;

import com.finstream.dto.TradeRequest;
import com.finstream.dto.TradeResponse;
import com.finstream.dto.mapper.TradeMapper;
import com.finstream.entity.Trade;
import com.finstream.entity.TradeAudit;
import com.finstream.entity.TradeStatus;
import com.finstream.exception.TradeException;
import com.finstream.repository.TradeAuditRepository;
import com.finstream.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {
    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);
    private final TradeRepository tradeRepo;
    private final TradeAuditRepository auditRepo;
    private final RiskPreCheckService riskService;
    private final LimitCheckService limitService;
    private final KafkaEventPublisher kafkaPublisher;
    private final TradeMapper mapper;

    @Override
    @Transactional
    public TradeResponse captureTrade(TradeRequest request) {

        if (tradeRepo.existsByTradeReference(request.getTradeReference())) {
            throw new TradeException("Duplicate trade reference");
        }

        // basic validation
        riskService.validate(request);

        double tradeValue = request.getPrice() * request.getQuantity();

        // limit checking
        limitService.checkLimit(request.getAccountId(), request.getQuantity(), tradeValue);

        // map to entity
        Trade trade = mapper.toEntity(request);
        trade.setStatus(TradeStatus.VALIDATED);
        trade.setTradeTimestamp(LocalDateTime.now());
        trade.setCreatedAt(LocalDateTime.now());

        Trade saved = tradeRepo.save(trade);

        // audit
        TradeAudit audit = TradeAudit.builder()
                .tradeReference(saved.getTradeReference())
                .action("CREATED")
                .timestamp(LocalDateTime.now())
                .performedBy("SYSTEM")
                .build();
        auditRepo.save(audit);

        // publish event
        kafkaPublisher.publishTradeEvent(saved);

        return mapper.toResponse(saved);
    }

    @Override
    public List<TradeResponse> captureTrades(List<TradeRequest> requests) {
        List<TradeResponse> responses = new ArrayList<>();
        for (TradeRequest request : requests) {
            if (tradeRepo.existsByTradeReference(request.getTradeReference())) {
                throw new TradeException("Duplicate Trade reference:" + request.getTradeReference());
            }
            riskService.validate(request);

            double tradeValue = request.getPrice() * request.getQuantity();
            limitService.checkLimit(request.getAccountId(), request.getQuantity(), tradeValue);

            Trade trade = mapper.toEntity(request);
            trade.setStatus(TradeStatus.VALIDATED);
            trade.setTradeTimestamp(LocalDateTime.now());
            trade.setCreatedAt(LocalDateTime.now());

            Trade saved = tradeRepo.save(trade);
            TradeAudit audit = TradeAudit.builder()
                    .tradeReference(saved.getTradeReference())
                    .action("CREATED")
                    .timestamp(LocalDateTime.now())
                    .performedBy("SYSTEM")
                    .build();
            auditRepo.save(audit);
            // 8. Publish Kafka event
            kafkaPublisher.publishTradeEvent(saved);

            // 9. Map to response
            responses.add(mapper.toResponse(saved));
        }

        return responses;
    }

    @Override
    public TradeResponse getTradeById(Long tradeId) {
        Trade trade = tradeRepo.findById(tradeId)
                .orElseThrow(() -> new TradeException("Trade not found with ID: " + tradeId));
        return mapper.toResponse(trade);
    }

    @Override
    public List<TradeResponse> getAllTrades(int page, int size,
                                            String accountId,
                                            String securityId,
                                            TradeStatus status) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Trade> tradePage;

        if (status == null) {
            tradePage =
                    tradeRepo.findByAccountIdContainingAndSecurityIdContainingAndStatus(
                            accountId != null ? accountId : "",
                            securityId != null ? securityId : "",status,
                            pageable
                    );
        } else {
            tradePage =
                    tradeRepo.findByAccountIdContainingAndSecurityIdContainingAndStatus(
                            accountId != null ? accountId : "",
                            securityId != null ? securityId : "",
                            status,
                            pageable
                    );
        }

        return tradePage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }



    public Map<String, Object> getTradeStatistics(String accountId) {
        List<Trade> trades = accountId == null ? tradeRepo.findAll() : tradeRepo.findByAccountId(accountId);
        double totalValue = trades.stream().mapToDouble(t -> t.getPrice() * t.getQuantity()).sum();
        double avgPrice = trades.stream().mapToDouble(Trade::getPrice).average().orElse(0);
        long totalTrades = trades.size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalValue", totalValue);
        stats.put("avgPrice", avgPrice);
        stats.put("totalTrades", totalTrades);
        return stats;
    }

    @Override
    public TradeResponse updateTrade(Long tradeId, TradeRequest request) {
        Trade trade = tradeRepo.findById(tradeId)
                .orElseThrow(() -> new TradeException("Trade not fount with this id" + tradeId));
        riskService.validate(request);
        double tradeValue = request.getPrice() * request.getQuantity();
        limitService.checkLimit(request.getAccountId(),request.getQuantity(),tradeValue);

        trade.setPrice(request.getPrice());
        trade.setQuantity(request.getQuantity());
        trade.setAccountId(request.getAccountId());
        trade.setSecurityId(request.getSecurityId());
        trade.setOrderType(request.getOrderType());
        trade.setTradeTimestamp(LocalDateTime.now());
        trade.setStatus(TradeStatus.VALIDATED);

        Trade updated=tradeRepo.save(trade);
        auditTrade(updated, "UPDATED");

        kafkaPublisher.publishTradeEvent(updated);
        logTrade(updated);
        return mapper.toResponse(updated);
    }

    @Override
    public void deleteTrade(Long tradeId) {
        Trade trade = tradeRepo.findById(tradeId)
                .orElseThrow(() -> new TradeException("Trade not found with ID: " + tradeId));
        tradeRepo.delete(trade);

        auditTrade(trade, "DELETED");
        kafkaPublisher.publishTradeEvent(trade); // optionally send delete event
        logger.info("Trade deleted: ID={}", trade.getTradeId());
    }

    private void logTrade(Trade trade) {
        logger.info("Trade captured: ID={} , Quantity={}, Price={}, Time={}",
                trade.getTradeId(),

                trade.getQuantity(),
                trade.getPrice(),
                LocalDateTime.now());
    }
    private void auditTrade(Trade trade, String action) {
        TradeAudit audit = TradeAudit.builder()
                .tradeReference(trade.getTradeReference())
                .action(action)
                .timestamp(LocalDateTime.now())
                .performedBy("SYSTEM")
                .build();
        auditRepo.save(audit);
    }

}

