package com.finstream.settlement.service;

import com.finstream.settlement.entity.SettlementAudit;
import com.finstream.settlement.repository.AuditRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final AuditRepository repo;

    public AuditService(AuditRepository repo) { this.repo = repo; }

    public void record(String tradeId, String action, String details) {
        SettlementAudit a = new SettlementAudit();
        a.setTradeId(tradeId);
        a.setAction(action);
        a.setDetails(details);
        repo.save(a);
    }
}
