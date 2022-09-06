package com.energy.consumption.fraction;

import com.energy.consumption.csv.model.FractionCsvRow;
import com.energy.consumption.csv.service.FractionCsvParser;
import com.energy.consumption.exception.model.ValidationException;
import com.energy.consumption.fraction.model.Fraction;
import com.energy.consumption.fraction.repository.FractionRepository;
import com.energy.consumption.fraction.service.FractionServiceImpl;
import com.energy.consumption.fraction.service.FractionValidationService;
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
public class FractionServiceUnitTests {

    @Mock
    private FractionRepository fractionRepository;
    @Mock
    private ProfileService profileService;
    @Mock
    private FractionCsvParser fractionCsvParser;
    @Mock
    private FractionValidationService fractionValidationService;
    @Mock
    private MapUtil mapUtil;

    @InjectMocks
    private FractionServiceImpl fractionService;

    @Captor
    private ArgumentCaptor<List<Fraction>> fractionsCaptor;

    @ParameterizedTest
    @EnumSource(Month.class)
    void processFile_successfully(Month month) throws IOException {
        MultipartFile file = getMockFile();
        FractionCsvRow fractionCsvRow = new FractionCsvRow(month, PROFILE_PUBLIC_ID, RATIO);
        List<FractionCsvRow> parsedRows = List.of(fractionCsvRow);
        Profile profile = mockProfile();

        when(fractionCsvParser.parseFile(file)).thenReturn(parsedRows);

        fractionService.processFile(file);

        verify(fractionValidationService).validateFractionsHaveRequiredSum(parsedRows);
        verify(fractionRepository).saveAll(fractionsCaptor.capture());

        List<Fraction> capturedFractions = fractionsCaptor.getValue();
        assertEquals(1, capturedFractions.size());

        Fraction capturedFraction = capturedFractions.get(0);
        assertEquals(month, capturedFraction.getMonthOfFraction());
        assertEquals(RATIO, capturedFraction.getRatio());
        assertEquals(profile, capturedFraction.getProfile());
    }

    private MultipartFile getMockFile() throws IOException {
        return new MockMultipartFile(FILE_NAME, InputStream.nullInputStream());
    }

    private Profile mockProfile() {
        Profile createdProfile = new Profile();
        createdProfile.setId(PROFILE_ID);

        when(profileService.createProfiles(Set.of(PROFILE_PUBLIC_ID))).thenReturn(List.of(createdProfile));
        when(mapUtil.groupByPublicId(List.of(createdProfile))).thenReturn(Map.of(PROFILE_PUBLIC_ID, createdProfile));

        return createdProfile;
    }

    @ParameterizedTest
    @EnumSource(Month.class)
    void getRatioByProfileIdAndMonth_fractionDoesNotExist_throwException(Month month) {
        assertThrows(ValidationException.class, () -> fractionService.getRatioByProfileIdAndMonth(PROFILE_ID, month));
    }

    @ParameterizedTest
    @EnumSource(Month.class)
    void getRatioByProfileIdAndMonth_fractionExists_returnRatio(Month month) {
        Fraction fraction = new Fraction();
        fraction.setRatio(RATIO);

        when(fractionRepository.findByProfileIdAndMonthOfFraction(PROFILE_ID, month)).thenReturn(Optional.of(fraction));

        Double ratio = fractionService.getRatioByProfileIdAndMonth(PROFILE_ID, month);

        assertEquals(RATIO, ratio);
    }
}
