package com.energy.consumption.fraction.service;

import com.energy.consumption.csv.model.FractionCsvRow;
import com.energy.consumption.exception.model.ValidationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class FractionValidationServiceImpl implements FractionValidationService {

    private static final Double REQUIRED_RATIO_SUM = 1.0;

    @Override
    public void validateFractionsHaveRequiredSum(List<FractionCsvRow> parsedRows) {
        Map<String, Double> ratiosGroupedByProfilePublicIds = groupRatiosByProfilePublicIds(parsedRows);

        for (Map.Entry<String, Double> profileRatio : ratiosGroupedByProfilePublicIds.entrySet()) {
            Double totalRatioOfProfile = profileRatio.getValue();

            if (!Objects.equals(totalRatioOfProfile, REQUIRED_RATIO_SUM)) {
                throw new ValidationException("Profile " + profileRatio.getKey() + " doesn't have the required sum of: " + REQUIRED_RATIO_SUM);
            }
        }
    }

    private Map<String, Double> groupRatiosByProfilePublicIds(List<FractionCsvRow> parsedRows) {
        Map<String, Double> profileTotalRatios = new HashMap<>();

        for (FractionCsvRow fraction : parsedRows) {
            Double ratio = profileTotalRatios.get(fraction.getProfilePublicId());

            if (ratio == null) {
                profileTotalRatios.put(fraction.getProfilePublicId(), fraction.getRatio());
                continue;
            }

            profileTotalRatios.put(fraction.getProfilePublicId(), ratio + fraction.getRatio());
        }

        return profileTotalRatios;
    }
}
