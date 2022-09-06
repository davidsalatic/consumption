package com.energy.consumption.csv.service;

import com.energy.consumption.csv.configuration.MeterReadingCsvConfiguration;
import com.energy.consumption.csv.model.MeterReadingCsvRow;
import com.energy.consumption.csv.util.MonthMapper;
import com.energy.consumption.exception.model.CsvParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Month;
import java.util.List;

import static com.energy.consumption.util.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeterReadingCsvParserUnitTests {

    @Mock
    private MeterReadingCsvConfiguration configuration;
    @Mock
    private MonthMapper monthMapper;

    @InjectMocks
    private MeterReadingCsvParserImpl meterReadingCsvParser;

    @Test
    void parseFile_exception_csvParseExceptionRethrown() {
        assertThrows(CsvParseException.class, () -> meterReadingCsvParser.parseFile(null));
    }

    @Test
    void parseFile_firstRowIsHeadline_skipFirstRowAndReturnResult() throws IOException {
        String csvText = "Headline\n"
                + METER_PUBLIC_ID + DELIMITER + PROFILE_PUBLIC_ID + DELIMITER + MONTH + DELIMITER + METER_VALUE;
        MultipartFile file = getMockFile(csvText);
        Month month = Month.JANUARY;

        when(configuration.isFirstRowTitle()).thenReturn(true);
        when(configuration.getDelimiter()).thenReturn(DELIMITER);
        when(monthMapper.getMonth(MONTH)).thenReturn(month);
        mockPositions();

        List<MeterReadingCsvRow> result = meterReadingCsvParser.parseFile(file);

        assertEquals(1, result.size());
        MeterReadingCsvRow parsedRow = result.get(0);

        assertEquals(METER_PUBLIC_ID, parsedRow.getMeterPublicId());
        assertEquals(PROFILE_PUBLIC_ID, parsedRow.getProfilePublicId());
        assertEquals(month, parsedRow.getMonth());
        assertEquals(METER_VALUE, parsedRow.getMeterValue());
    }

    @Test
    void parseFile_firstRowIsNotHeadline_returnResult() throws IOException {
        String csvText = METER_PUBLIC_ID + DELIMITER + PROFILE_PUBLIC_ID + DELIMITER + MONTH + DELIMITER + METER_VALUE;
        MultipartFile file = getMockFile(csvText);
        Month month = Month.JANUARY;

        when(configuration.getDelimiter()).thenReturn(DELIMITER);
        when(monthMapper.getMonth(MONTH)).thenReturn(month);
        mockPositions();

        List<MeterReadingCsvRow> result = meterReadingCsvParser.parseFile(file);

        assertEquals(1, result.size());
        MeterReadingCsvRow parsedRow = result.get(0);

        assertEquals(METER_PUBLIC_ID, parsedRow.getMeterPublicId());
        assertEquals(PROFILE_PUBLIC_ID, parsedRow.getProfilePublicId());
        assertEquals(month, parsedRow.getMonth());
        assertEquals(METER_VALUE, parsedRow.getMeterValue());
    }

    private MultipartFile getMockFile(String csvText) throws IOException {
        InputStream targetStream = new ByteArrayInputStream(csvText.getBytes());

        return new MockMultipartFile(FILE_NAME, targetStream);
    }

    private void mockPositions() {
        when(configuration.getMeterIdPosition()).thenReturn(0);
        when(configuration.getProfileIdPosition()).thenReturn(1);
        when(configuration.getMonthPosition()).thenReturn(2);
        when(configuration.getMeterReadingPosition()).thenReturn(3);
    }
}
