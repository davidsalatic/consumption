package com.energy.consumption.meterreading.service;

import com.energy.consumption.csv.model.MeterReadingCsvRow;
import com.energy.consumption.exception.model.ValidationException;
import com.energy.consumption.fraction.service.FractionService;
import com.energy.consumption.profile.model.Profile;
import com.energy.consumption.profile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.util.List;
import java.util.Set;

import static com.energy.consumption.util.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeterReadingValidationServiceUnitTests {

    @Mock
    private ProfileService profileService;
    @Mock
    private FractionService fractionService;

    @InjectMocks
    private MeterReadingValidationServiceImpl meterReadingValidationService;

    @Test
    void validateProfilesExist_allProfilesDoNotExist_throwException() {
        String nonExistingProfileId = "nonExistingProfileId";
        Set<String> profilePublicIds = Set.of(PROFILE_PUBLIC_ID, nonExistingProfileId);
        Profile existingProfile = new Profile();
        existingProfile.setPublicId(PROFILE_PUBLIC_ID);

        when(profileService.getByPublicIds(profilePublicIds)).thenReturn(List.of(existingProfile));

        assertThrows(ValidationException.class, () -> meterReadingValidationService.validateProfilesExist(profilePublicIds));
    }

    @Test
    void validateProfilesExist_allProfilesExist_successfully() {
        Set<String> profilePublicIds = Set.of(PROFILE_PUBLIC_ID);
        Profile existingProfile = new Profile();
        existingProfile.setPublicId(PROFILE_PUBLIC_ID);

        when(profileService.getByPublicIds(profilePublicIds)).thenReturn(List.of(existingProfile));

        assertDoesNotThrow(() -> meterReadingValidationService.validateProfilesExist(profilePublicIds));
    }

    @Test
    void getInvalidMeterPublicIds_meterValueDecreasing_returnIdAsInvalid() {
        double firstReadingValue = 1;
        double decreasedValue = firstReadingValue - 0.1;
        MeterReadingCsvRow firstReading = new MeterReadingCsvRow(METER_PUBLIC_ID, PROFILE_PUBLIC_ID, Month.JANUARY, firstReadingValue);
        MeterReadingCsvRow invalidReading = new MeterReadingCsvRow(METER_PUBLIC_ID, PROFILE_PUBLIC_ID, Month.FEBRUARY, decreasedValue);
        Profile profile = new Profile();
        profile.setId(PROFILE_ID);

        when(profileService.getByPublicId(PROFILE_PUBLIC_ID)).thenReturn(profile);
        when(fractionService.getRatioByProfileIdAndMonth(PROFILE_ID, Month.JANUARY)).thenReturn(1.0);

        Set<String> invalidMeterPublicIds = meterReadingValidationService.getInvalidMeterPublicIds(List.of(invalidReading, firstReading));

        assertEquals(1, invalidMeterPublicIds.size());
    }

    @Test
    void getInvalidMeterPublicIds_consumptionNotInToleratedRange_returnIdAsInvalid() {
        MeterReadingCsvRow firstReading = new MeterReadingCsvRow(METER_PUBLIC_ID, PROFILE_PUBLIC_ID, Month.JANUARY, METER_VALUE);
        MeterReadingCsvRow invalidReading = new MeterReadingCsvRow(METER_PUBLIC_ID, PROFILE_PUBLIC_ID, Month.FEBRUARY, METER_VALUE * 2);
        Profile profile = new Profile();
        profile.setId(PROFILE_ID);

        when(profileService.getByPublicId(PROFILE_PUBLIC_ID)).thenReturn(profile);
        when(fractionService.getRatioByProfileIdAndMonth(PROFILE_ID, Month.JANUARY)).thenReturn(0.5);
        when(fractionService.getRatioByProfileIdAndMonth(PROFILE_ID, Month.FEBRUARY)).thenReturn(1.0);

        Set<String> invalidMeterPublicIds = meterReadingValidationService.getInvalidMeterPublicIds(List.of(firstReading, invalidReading));

        assertEquals(1, invalidMeterPublicIds.size());
    }

    @Test
    void getInvalidMeterPublicIds_consumptionsValid_returnEmptyList() {
        MeterReadingCsvRow firstReading = new MeterReadingCsvRow(METER_PUBLIC_ID, PROFILE_PUBLIC_ID, Month.JANUARY, METER_VALUE);
        MeterReadingCsvRow secondReading = new MeterReadingCsvRow(METER_PUBLIC_ID, PROFILE_PUBLIC_ID, Month.FEBRUARY, METER_VALUE * 2);
        Profile profile = new Profile();
        profile.setId(PROFILE_ID);

        when(profileService.getByPublicId(PROFILE_PUBLIC_ID)).thenReturn(profile);
        when(fractionService.getRatioByProfileIdAndMonth(PROFILE_ID, Month.JANUARY)).thenReturn(0.5);
        when(fractionService.getRatioByProfileIdAndMonth(PROFILE_ID, Month.FEBRUARY)).thenReturn(0.5);

        Set<String> invalidMeterPublicIds = meterReadingValidationService.getInvalidMeterPublicIds(List.of(firstReading, secondReading));

        assertEquals(0, invalidMeterPublicIds.size());
    }
}
