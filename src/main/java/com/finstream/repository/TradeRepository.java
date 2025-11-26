package com.finstream.repository;

import com.finstream.entity.Trade;
import com.finstream.entity.TradeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    boolean existsByTradeReference(String tradeReference);
    Optional<Trade> findByTradeReference(String tradeReference);

    List<Trade> findByAccountId(String accountId);

    Page<Trade> findByAccountIdContainingAndSecurityIdContainingAndStatus(
            String accountId,
            String securityId,
            TradeStatus status,
            Pageable pageable
    );
    @Query("SELECT t FROM Trade t " +
            "WHERE (:accountId IS NULL OR t.accountId = :accountId) " +
            "AND (:securityId IS NULL OR t.securityId = :securityId) " +
            "AND (:status IS NULL OR t.status = :status)")
    List<Trade> findByFilters(@Param("accountId") String accountId,
                              @Param("securityId") String securityId,
                              @Param("status") TradeStatus status,
                              Pageable pageable);
}
