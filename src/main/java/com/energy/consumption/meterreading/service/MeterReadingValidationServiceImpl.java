package com.energy.consumption.meterreading.service;

import com.energy.consumption.csv.model.MeterReadingCsvRow;
import com.energy.consumption.exception.model.ValidationException;
import com.energy.consumption.fraction.service.FractionService;
import com.energy.consumption.profile.model.Profile;
import com.energy.consumption.profile.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MeterReadingValidationServiceImpl implements MeterReadingValidationService {

    private final ProfileService profileService;
    private final FractionService fractionService;

    private final Logger LOGGER = LoggerFactory.getLogger(MeterReadingValidationServiceImpl.class);

    private static final double CONSUMPTION_TOLERANCE = 0.25;

    public MeterReadingValidationServiceImpl(ProfileService profileService, FractionService fractionService) {
        this.profileService = profileService;
        this.fractionService = fractionService;
    }

    @Override
    public void validateProfilesExist(Set<String> profilePublicIds) {
        List<Profile> existingProfiles = profileService.getByPublicIds(profilePublicIds);

        if (existingProfiles.size() != profilePublicIds.size()) {
            throw new ValidationException("Profiles have to be created first!");
        }
    }

    @Override
    public Set<String> getInvalidMeterPublicIds(List<MeterReadingCsvRow> parsedRows) {
        Map<String, List<MeterReadingCsvRow>> meterReadingsGroupedByPublicMeterId = groupMeterReadingsByPublicMeterIds(parsedRows);

        Set<String> invalidMeterExternalIds = new HashSet<>();

        for (Map.Entry<String, List<MeterReadingCsvRow>> meterReading : meterReadingsGroupedByPublicMeterId.entrySet()) {
            List<MeterReadingCsvRow> meterReadings = meterReading.getValue();
            meterReadings.sort(Comparator.comparing(MeterReadingCsvRow::getMonth));

            double totalConsumption = meterReadings.get(meterReadings.size() - 1).getMeterValue();

            double previousMeterValue = 0;

            for (MeterReadingCsvRow reading : meterReadings) {
                if (reading.getMeterValue() < previousMeterValue) {
                    LOGGER.error("Skipping meter {}. Meter value of previous month is higher then of month: {}", reading.getMeterPublicId(), reading.getMonth());
                    invalidMeterExternalIds.add(reading.getMeterPublicId());
                    break;
                }

                boolean consumptionInToleratedRange = isConsumptionInToleratedRange(reading, previousMeterValue, totalConsumption);
                if (!consumptionInToleratedRange) {
                    invalidMeterExternalIds.add(reading.getMeterPublicId());
                    break;
                }

                previousMeterValue = reading.getMeterValue();
            }
        }

        return invalidMeterExternalIds;
    }

    private Map<String, List<MeterReadingCsvRow>> groupMeterReadingsByPublicMeterIds(List<MeterReadingCsvRow> parsedRows) {
        Map<String, List<MeterReadingCsvRow>> meterPublicIdMeterReadingPair = new HashMap<>();

        for (MeterReadingCsvRow meterReading : parsedRows) {
            List<MeterReadingCsvRow> meterReadings = meterPublicIdMeterReadingPair.get(meterReading.getMeterPublicId());
            if (meterReadings == null) {
                meterPublicIdMeterReadingPair.put(meterReading.getMeterPublicId(), new ArrayList<>(List.of(meterReading)));
                continue;
            }

            meterReadings.add(meterReading);
        }

        return meterPublicIdMeterReadingPair;
    }

    private boolean isConsumptionInToleratedRange(MeterReadingCsvRow meterReading, double previousMeterValue, double totalConsumption) {
        double currentConsumption = meterReading.getMeterValue() - previousMeterValue;

        Profile profile = profileService.getByPublicId(meterReading.getProfilePublicId());
        Double ratio = fractionService.getRatioByProfileIdAndMonth(profile.getId(), meterReading.getMonth());

        double expectedConsumption = totalConsumption * ratio;
        double minimumConsumption = expectedConsumption - expectedConsumption * CONSUMPTION_TOLERANCE;
        double maximumConsumption = expectedConsumption + expectedConsumption * CONSUMPTION_TOLERANCE;

        boolean consumptionInToleratedRange = currentConsumption >= minimumConsumption && currentConsumption <= maximumConsumption;

        if (!consumptionInToleratedRange) {
            LOGGER.error("Meter {} consumption is invalid. Expected consumption is between: {} and {}, but got {}!", meterReading.getMeterPublicId(), minimumConsumption, maximumConsumption, currentConsumption);
        }

        return consumptionInToleratedRange;
    }
}
