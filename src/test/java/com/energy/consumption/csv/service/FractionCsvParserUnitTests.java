package com.energy.consumption.csv.service;

import com.energy.consumption.csv.configuration.FractionsCsvConfiguration;
import com.energy.consumption.csv.model.FractionCsvRow;
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
public class FractionCsvParserUnitTests {

    @Mock
    private FractionsCsvConfiguration configuration;
    @Mock
    private MonthMapper monthMapper;

    @InjectMocks
    private FractionCsvParserImpl fractionCsvParser;

    @Test
    void parseFile_exception_csvParseExceptionRethrown() {
        assertThrows(CsvParseException.class, () -> fractionCsvParser.parseFile(null));
    }

    @Test
    void parseFile_firstRowIsHeadline_skipFirstRowAndReturnResult() throws IOException {
        String csvText = "Headline\n"
                + MONTH + DELIMITER + PROFILE_PUBLIC_ID + DELIMITER + RATIO;
        MultipartFile file = getMockFile(csvText);
        Month month = Month.JANUARY;

        when(configuration.isFirstRowTitle()).thenReturn(true);
        when(configuration.getDelimiter()).thenReturn(DELIMITER);
        when(monthMapper.getMonth(MONTH)).thenReturn(month);
        mockPositions();

        List<FractionCsvRow> result = fractionCsvParser.parseFile(file);

        assertEquals(1, result.size());
        FractionCsvRow parsedRow = result.get(0);

        assertEquals(month, parsedRow.getMonth());
        assertEquals(PROFILE_PUBLIC_ID, parsedRow.getProfilePublicId());
        assertEquals(RATIO, parsedRow.getRatio());
    }

    @Test
    void parseFile_firstRowIsNotHeadline_returnResult() throws IOException {
        String csvText = MONTH + DELIMITER + PROFILE_PUBLIC_ID + DELIMITER + RATIO;
        MultipartFile file = getMockFile(csvText);
        Month month = Month.JANUARY;

        when(configuration.getDelimiter()).thenReturn(DELIMITER);
        when(monthMapper.getMonth(MONTH)).thenReturn(month);
        mockPositions();

        List<FractionCsvRow> result = fractionCsvParser.parseFile(file);

        assertEquals(1, result.size());
        FractionCsvRow parsedRow = result.get(0);

        assertEquals(month, parsedRow.getMonth());
        assertEquals(PROFILE_PUBLIC_ID, parsedRow.getProfilePublicId());
        assertEquals(RATIO, parsedRow.getRatio());
    }

    private MultipartFile getMockFile(String csvText) throws IOException {
        InputStream targetStream = new ByteArrayInputStream(csvText.getBytes());

        return new MockMultipartFile(FILE_NAME, targetStream);
    }

    private void mockPositions() {
        when(configuration.getMonthPosition()).thenReturn(0);
        when(configuration.getProfileIdPosition()).thenReturn(1);
        when(configuration.getRatioPosition()).thenReturn(2);
    }
}
