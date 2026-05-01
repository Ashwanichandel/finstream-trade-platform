package com.finstream.settlement.controller;

import com.finstream.settlement.dto.TradeDTO;
import com.finstream.settlement.entity.SettlementEntity;
import com.finstream.settlement.enums.SettlementState;
import com.finstream.settlement.repository.SettlementRepository;
import com.finstream.settlement.service.SettlementProcessor;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementProcessor settlementProcessor;
    private final SettlementRepository settlementRepository;

    public SettlementController(SettlementProcessor settlementProcessor, SettlementRepository settlementRepository) {
        this.settlementProcessor = settlementProcessor;
        this.settlementRepository = settlementRepository;
    }
    @PostMapping("/process")
    public ResponseEntity<?> process(@RequestBody @Valid TradeDTO tradeDTO){
        SettlementEntity settlementEntity = settlementProcessor.processTrade(tradeDTO);
       return ResponseEntity.ok(settlementEntity);
    }


}
