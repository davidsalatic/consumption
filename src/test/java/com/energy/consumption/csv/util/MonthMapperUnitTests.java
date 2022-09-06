package com.energy.consumption.csv.util;

import com.energy.consumption.csv.configuration.MonthCsvConfiguration;
import com.energy.consumption.exception.model.ValidationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;

import static com.energy.consumption.util.TestConstants.MONTH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@ExtendWith(MockitoExtension.class)
@TestInstance(PER_CLASS)
public class MonthMapperUnitTests {

    private MonthMapper monthMapper;

    @BeforeAll
    public void setup() {
        MonthCsvConfiguration configuration = new MonthCsvConfiguration();
        configuration.setJanuary(MONTH);

        monthMapper = new MonthMapper(configuration);
    }

    @Test
    void getMonth_noMappingFound_throwException() {
        assertThrows(ValidationException.class, () -> monthMapper.getMonth("nonExistingMapping"));
    }

    @Test
    void getMonth_mappingExists_returnMappedMonth() {
        assertEquals(Month.JANUARY, monthMapper.getMonth(MONTH));
    }
}
