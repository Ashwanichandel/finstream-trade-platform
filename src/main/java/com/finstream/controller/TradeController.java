package com.finstream.controller;

import com.finstream.dto.TradeRequest;
import com.finstream.dto.TradeResponse;
import com.finstream.entity.TradeStatus;
import com.finstream.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/capture")
    public ResponseEntity<TradeResponse> capture(@Validated @RequestBody TradeRequest request) {
        TradeResponse resp = tradeService.captureTrade(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/captures")
    public ResponseEntity<List<TradeResponse>> saveTrades(@RequestBody List<TradeRequest> request) {
        List<TradeResponse> savedTrades = tradeService.captureTrades(request);
        return ResponseEntity.ok(savedTrades);
    }

    @GetMapping("/{tradeId}")
    public ResponseEntity<TradeResponse> getTrade(@PathVariable Long tradeId) {
        TradeResponse response = tradeService.getTradeById(tradeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAlltrade")
    public ResponseEntity<List<TradeResponse>> getAllTrade(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(required = false) String accountId,
                                                           @RequestParam(required = false) String securityId,
                                                           @RequestParam(required = false) TradeStatus status) {
        List<TradeResponse> trades = tradeService.getAllTrades(page, size, accountId, securityId, status);
        return ResponseEntity.ok(trades);
    }

    @PutMapping("/{tradeId}")
    public ResponseEntity<TradeResponse> updateTrade(@PathVariable Long tradeId,
                                                     @RequestBody TradeRequest request) {
        TradeResponse updated = tradeService.updateTrade(tradeId, request);
        return ResponseEntity.ok(updated);
    }

    // ---------------- Delete Trade ----------------
    @DeleteMapping("/{tradeId}")
    public ResponseEntity<Void> deleteTrade(@PathVariable Long tradeId) {
        tradeService.deleteTrade(tradeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getTradeStats(
            @RequestParam(required = false) String accountId) {
        Map<String, Object> stats = tradeService.getTradeStatistics(accountId);
        return ResponseEntity.ok(stats);
    }


}
