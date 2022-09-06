package com.energy.consumption.fraction;

import com.energy.consumption.csv.model.FractionCsvRow;
import com.energy.consumption.exception.model.ValidationException;
import com.energy.consumption.fraction.service.FractionValidationServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.util.List;

import static com.energy.consumption.util.TestConstants.PROFILE_PUBLIC_ID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class FractionValidationServiceUnitTests {

    @InjectMocks
    private FractionValidationServiceImpl fractionValidationService;

    @ParameterizedTest
    @EnumSource(Month.class)
    void validateFractionsHaveRequiredSum_ratiosDoNotSumUpToRequired_throwException(Month month) {
        double firstFractionRatio = 0.5;
        double secondFractionRatio = 0.4;

        FractionCsvRow firstFraction = new FractionCsvRow(month, PROFILE_PUBLIC_ID, firstFractionRatio);
        FractionCsvRow secondFraction = new FractionCsvRow(month, PROFILE_PUBLIC_ID, secondFractionRatio);

        assertThrows(ValidationException.class, () -> fractionValidationService.validateFractionsHaveRequiredSum(List.of(firstFraction, secondFraction)));
    }

    @ParameterizedTest
    @EnumSource(Month.class)
    void validateFractionsHaveRequiredSum_ratiosSumUpToRequired_passValidation(Month month) {
        double firstFractionRatio = 0.5;
        double secondFractionRatio = 0.5;

        FractionCsvRow firstFraction = new FractionCsvRow(month, PROFILE_PUBLIC_ID, firstFractionRatio);
        FractionCsvRow secondFraction = new FractionCsvRow(month, PROFILE_PUBLIC_ID, secondFractionRatio);

        assertDoesNotThrow(() -> fractionValidationService.validateFractionsHaveRequiredSum(List.of(firstFraction, secondFraction)));
    }
}
