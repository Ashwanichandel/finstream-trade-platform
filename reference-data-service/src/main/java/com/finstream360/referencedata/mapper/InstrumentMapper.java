package com.finstream360.referencedata.mapper;

import com.finstream360.referencedata.dto.InstrumentDTO;
import com.finstream360.referencedata.entity.Instrument;

public interface InstrumentMapper {
    Instrument toEntity(InstrumentDTO dto);
    InstrumentDTO toDto(Instrument entity);
}
