package com.energy.consumption.meterreading.service;

import com.energy.consumption.csv.model.MeterReadingCsvRow;
import com.energy.consumption.csv.service.MeterReadingCsvParser;
import com.energy.consumption.exception.model.ValidationException;
import com.energy.consumption.meter.model.Meter;
import com.energy.consumption.meter.service.MeterService;
import com.energy.consumption.meterreading.model.MeterReading;
import com.energy.consumption.meterreading.repository.MeterReadingRepository;
import com.energy.consumption.profile.model.Profile;
import com.energy.consumption.profile.service.ProfileService;
import com.energy.consumption.util.MapUtil;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.energy.consumption.util.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeterReadingServiceUnitTests {

    @Mock
    private MeterService meterService;
    @Mock
    private ProfileService profileService;
    @Mock
    private MapUtil mapUtil;
    @Mock
    private MeterReadingRepository meterReadingRepository;
    @Mock
    private MeterReadingCsvParser meterReadingCsvParser;
    @Mock
    private MeterReadingValidationService meterReadingValidationService;

    @InjectMocks
    private MeterReadingServiceImpl meterReadingService;

    @Captor
    private ArgumentCaptor<List<MeterReading>> meterReadingsCaptor;

    @ParameterizedTest
    @EnumSource(Month.class)
    void processFile_filterOutInvalidAndSaveSuccessfully(Month month) throws IOException {
        MultipartFile file = getMockFile();
        String invalidMeterPublicId = "invalidMeterPublicId";
        MeterReadingCsvRow validMeterData = new MeterReadingCsvRow(METER_PUBLIC_ID, PROFILE_PUBLIC_ID, month, METER_VALUE);
        MeterReadingCsvRow invalidMeterData = new MeterReadingCsvRow(invalidMeterPublicId, PROFILE_PUBLIC_ID, month, METER_VALUE);
        Meter createdMeter = mockCreatedMeter();
        Profile profile = mockExistingProfile();

        when(meterReadingCsvParser.parseFile(file)).thenReturn(List.of(validMeterData, invalidMeterData));
        when(meterReadingValidationService.getInvalidMeterPublicIds(List.of(validMeterData, invalidMeterData))).thenReturn(Set.of(invalidMeterPublicId));

        meterReadingService.processFile(file);

        verify(meterReadingValidationService).validateProfilesExist(Set.of(PROFILE_PUBLIC_ID));
        verify(meterReadingRepository).saveAll(meterReadingsCaptor.capture());

        List<MeterReading> capturedMeterReadings = meterReadingsCaptor.getValue();
        assertEquals(1, capturedMeterReadings.size());

        MeterReading capturedMeterReading = capturedMeterReadings.get(0);
        assertEquals(month, capturedMeterReading.getMonthOfReading());
        assertEquals(METER_VALUE, capturedMeterReading.getMeterValue());
        assertEquals(createdMeter, capturedMeterReading.getMeter());
        assertEquals(profile, capturedMeterReading.getProfile());
    }

    private Meter mockCreatedMeter() {
        Meter createdMeter = new Meter();
        createdMeter.setId(METER_ID);
        List<Meter> createdMeters = List.of(createdMeter);

        when(meterService.create(Set.of(METER_PUBLIC_ID))).thenReturn(createdMeters);
        when(mapUtil.groupByPublicId(createdMeters)).thenReturn(Map.of(METER_PUBLIC_ID, createdMeter));

        return createdMeter;
    }

    private Profile mockExistingProfile() {
        Profile existingProfile = new Profile();
        existingProfile.setId(PROFILE_ID);
        List<Profile> existingProfiles = List.of(existingProfile);

        when(profileService.getByPublicIds(Set.of(PROFILE_PUBLIC_ID))).thenReturn(existingProfiles);
        when(mapUtil.groupByPublicId(existingProfiles)).thenReturn(Map.of(PROFILE_PUBLIC_ID, existingProfile));

        return existingProfile;
    }

    private MultipartFile getMockFile() throws IOException {
        return new MockMultipartFile(FILE_NAME, InputStream.nullInputStream());
    }

    @ParameterizedTest
    @EnumSource(Month.class)
    void getByMeterIdAndMonth_meterReadingDoesNotExist_throwException(Month month) {
        assertThrows(ValidationException.class, () -> meterReadingService.getByMeterPublicIdAndMonth(METER_ID, month));
    }

    @ParameterizedTest
    @EnumSource(Month.class)
    void getByMeterIdAndMonth_successfully(Month month) {
        MeterReading expectedMeterReading = new MeterReading();

        when(meterReadingRepository.findByMeterPublicIdAndMonthOfReading(METER_ID, month)).thenReturn(Optional.of(expectedMeterReading));

        MeterReading actualMeterReading = meterReadingService.getByMeterPublicIdAndMonth(METER_ID, month);

        assertEquals(expectedMeterReading, actualMeterReading);
    }
}
