package com.energy.consumption.meter.service;

import com.energy.consumption.meter.model.Meter;
import com.energy.consumption.meter.repository.MeterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.energy.consumption.util.TestConstants.METER_PUBLIC_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeterServiceUnitTests {

    @Mock
    private MeterRepository meterRepository;

    @InjectMocks
    private MeterServiceImpl meterService;

    @Captor
    private ArgumentCaptor<List<Meter>> metersCaptor;

    @Test
    void create_successfully() {
        List<Meter> expectedMeters = new ArrayList<>();

        when(meterRepository.saveAll(metersCaptor.capture())).thenReturn(expectedMeters);

        List<Meter> actualMeters = meterService.create(Set.of(METER_PUBLIC_ID));

        List<Meter> capturedMeters = metersCaptor.getValue();
        assertEquals(1, capturedMeters.size());
        Meter capturedMeter = capturedMeters.get(0);
        assertEquals(METER_PUBLIC_ID, capturedMeter.getPublicId());

        assertEquals(expectedMeters, actualMeters);
    }
}
