package com.energy.consumption.meter.service;

import com.energy.consumption.meter.model.Meter;
import com.energy.consumption.meter.repository.MeterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MeterServiceImpl implements MeterService {

    private final MeterRepository meterRepository;

    public MeterServiceImpl(MeterRepository meterRepository) {
        this.meterRepository = meterRepository;
    }

    @Override
    public List<Meter> create(Set<String> publicIds) {
        List<Meter> meterToSave = publicIds.stream()
                .map(this::toMeter)
                .collect(Collectors.toList());

        return meterRepository.saveAll(meterToSave);
    }

    private Meter toMeter(String publicId) {
        Meter meter = new Meter();
        meter.setPublicId(publicId);

        return meter;
    }
}
