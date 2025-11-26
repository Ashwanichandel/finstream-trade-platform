package com.finstream.service;

import com.finstream.entity.CounterpartyLimit;
import com.finstream.exception.LimitExceededException;
import com.finstream.repository.CounterpartyLimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LimitCheckServiceImpl implements LimitCheckService {

    private final CounterpartyLimitRepository repo;

    @Override
    public void checkLimit(String accountId, Integer quantity, Double tradeValue) {

        CounterpartyLimit limit = repo.findByAccountId(accountId)
                .orElseGet(() -> {
                    // Auto-create default limit (POC / Demo)
                    CounterpartyLimit defaultLimit = new CounterpartyLimit();
                    defaultLimit.setAccountId(accountId);
                    defaultLimit.setMaxLimit(1_000_000.0); // 10 lakh / 1 Cr demo
                    defaultLimit.setUsedLimit(0.0);
                    return repo.save(defaultLimit);
                });

        Double newExposure = limit.getUsedLimit() + tradeValue;

        if (newExposure > limit.getMaxLimit()) {
            throw new LimitExceededException(
                    "Exposure limit exceeded for account: " + accountId +
                            ". Max=" + limit.getMaxLimit() +
                            ", Used=" + limit.getUsedLimit()
            );
        }

        // consume limit
        limit.setUsedLimit(newExposure);
        repo.save(limit);
    }
}