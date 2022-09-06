package com.energy.consumption.consumption.service;

import java.time.Month;

public interface ConsumptionService {
    Double getConsumption(String meterPublicId, Month month);
}
