package com.finstream360.referencedata.service;

import com.finstream360.referencedata.dto.InstrumentDTO;

import java.util.List;

public interface InstrumentService {
    InstrumentDTO create(InstrumentDTO dto);
    InstrumentDTO update(Long id, InstrumentDTO dto);
    InstrumentDTO getById(Long id);
    InstrumentDTO getByIsin(String isin);
    List<InstrumentDTO> listAll();
    void delete(Long id);
}
