package com.finstream.repository;




import com.finstream.entity.TradeAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TradeAuditRepository extends JpaRepository<TradeAudit, Long> {
    List<TradeAudit> findByTradeReferenceOrderByTimestampDesc(String tradeReference);
}
