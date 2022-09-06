package com.energy.consumption.meter.service;

import com.energy.consumption.meter.model.Meter;

import java.util.List;
import java.util.Set;

public interface MeterService {
    List<Meter> create(Set<String> publicIds);
}
