package com.energy.consumption.meterreading.service;

import com.energy.consumption.csv.model.MeterReadingCsvRow;

import java.util.List;
import java.util.Set;

public interface MeterReadingValidationService {
    void validateProfilesExist(Set<String> profilePublicIds);

    Set<String> getInvalidMeterPublicIds(List<MeterReadingCsvRow> parsedRows);
}
