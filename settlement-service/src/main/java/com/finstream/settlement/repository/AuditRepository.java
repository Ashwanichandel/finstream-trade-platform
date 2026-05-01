package com.finstream.settlement.repository;

import com.finstream.settlement.entity.SettlementAudit;
import com.finstream.settlement.entity.SettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository  extends JpaRepository<SettlementAudit,Long> {
}
