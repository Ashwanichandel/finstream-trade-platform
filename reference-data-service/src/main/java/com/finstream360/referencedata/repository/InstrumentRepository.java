package com.finstream360.referencedata.repository;

import com.finstream360.referencedata.entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    Optional<Instrument> findByIsin(String isin);
    Optional<Instrument> findBySymbol(String symbol);
}
