package com.finstream360.referencedata.service;

import com.finstream360.referencedata.dto.InstrumentDTO;
import com.finstream360.referencedata.entity.Instrument;
import com.finstream360.referencedata.events.RefDataEvent;
import com.finstream360.referencedata.mapper.InstrumentMapper;
import com.finstream360.referencedata.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {
    private final InstrumentMapper mapper;
    private final InstrumentRepository instrumentRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public InstrumentDTO create(InstrumentDTO dto) {
        Instrument entity = mapper.toEntity(dto);
        Instrument save = instrumentRepository.save(entity);
        publishChangeEvent("CREATED", save);

        return null;
    }

    @Override
    public InstrumentDTO update(Long id, InstrumentDTO dto) {
        return null;
    }

    @Override
    public InstrumentDTO getById(Long id) {
        return null;
    }

    @Override
    public InstrumentDTO getByIsin(String isin) {
        return null;
    }

    @Override
    public List<InstrumentDTO> listAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }

    private void publishChangeEvent(String action, Instrument instrument) {
        try {
            RefDataEvent event = RefDataEvent.builder()
                    .entity("instrument")
                    .action(action)
                    .id(instrument.getId())
                    .payload(objectMapper.writeValueAsString(instrument))
                    .build()
        } catch (Exception e) {

        }
    }
}
