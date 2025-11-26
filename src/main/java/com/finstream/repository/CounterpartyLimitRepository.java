package com.finstream.repository;




import com.finstream.entity.CounterpartyLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CounterpartyLimitRepository extends JpaRepository<CounterpartyLimit, Long> {
    Optional<CounterpartyLimit> findByAccountId(String accountId);
}
