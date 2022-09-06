package com.energy.consumption.consumption.service;

import com.energy.consumption.meterreading.model.MeterReading;
import com.energy.consumption.meterreading.service.MeterReadingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;

import static com.energy.consumption.util.TestConstants.METER_PUBLIC_ID;
import static com.energy.consumption.util.TestConstants.METER_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsumptionServiceUnitTests {

    @Mock
    private MeterReadingService meterReadingService;

    @InjectMocks
    private ConsumptionServiceImpl consumptionService;

    @Test
    void getConsumption_january_returnJanuaryMeterValue() {
        mockMeterReading(Month.JANUARY, METER_VALUE);

        double consumption = consumptionService.getConsumption(METER_PUBLIC_ID, Month.JANUARY);

        assertEquals(METER_VALUE, consumption);
    }

    @ParameterizedTest
    @EnumSource(value = Month.class, mode = EXCLUDE, names = "JANUARY")
    void getConsumption_monthsOtherThanJanuary_returnDifferenceWithPreviousMonth(Month month) {
        double currentMonthMeterValue = 1234;
        double previousMonthMeterValue = 56;

        mockMeterReading(month, currentMonthMeterValue);
        mockMeterReading(Month.of(month.getValue() - 1), previousMonthMeterValue);

        double actualConsumption = consumptionService.getConsumption(METER_PUBLIC_ID, month);

        double expectedConsumption = currentMonthMeterValue - previousMonthMeterValue;

        assertEquals(expectedConsumption, actualConsumption);
    }

    private void mockMeterReading(Month month, double meterValue) {
        MeterReading meterReading = new MeterReading();
        meterReading.setMeterValue(meterValue);

        when(meterReadingService.getByMeterPublicIdAndMonth(METER_PUBLIC_ID, month)).thenReturn(meterReading);
    }
}
