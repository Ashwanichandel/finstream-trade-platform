package com.finstream.settlement.repository;

import com.finstream.settlement.entity.SettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SettlementRepository extends JpaRepository<SettlementEntity, Long> {
    Optional<SettlementEntity> findByTradeId(String tradeId);
    List<SettlementEntity> findByState(String state);
}
