package com.energy.consumption.fraction.service;

import com.energy.consumption.csv.model.FractionCsvRow;

import java.util.List;

public interface FractionValidationService {
    void validateFractionsHaveRequiredSum(List<FractionCsvRow> parsedRows);
}
