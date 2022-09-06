package com.energy.consumption.consumption.controller;

import com.energy.consumption.consumption.service.ConsumptionService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Month;

import static com.energy.consumption.util.TestConstants.CONSUMPTION;
import static com.energy.consumption.util.TestConstants.METER_PUBLIC_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsumptionControllerUnitTests {

    @Mock
    private ConsumptionService consumptionService;

    @InjectMocks
    private ConsumptionController consumptionController;

    @ParameterizedTest
    @EnumSource(Month.class)
    void getConsumption_successfully(Month month) {
        when(consumptionService.getConsumption(METER_PUBLIC_ID, month)).thenReturn(CONSUMPTION);

        ResponseEntity<Double> response = consumptionController.getConsumption(METER_PUBLIC_ID, month);

        assertEquals(CONSUMPTION, response.getBody());
    }
}
