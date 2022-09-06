package com.energy.consumption.csv.service;

import com.energy.consumption.csv.configuration.MeterReadingCsvConfiguration;
import com.energy.consumption.csv.model.MeterReadingCsvRow;
import com.energy.consumption.csv.util.MonthMapper;
import com.energy.consumption.exception.model.CsvParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeterReadingCsvParserImpl implements MeterReadingCsvParser {

    private final MonthMapper monthMapper;
    private final MeterReadingCsvConfiguration configuration;

    public MeterReadingCsvParserImpl(MonthMapper monthMapper, MeterReadingCsvConfiguration configuration) {
        this.monthMapper = monthMapper;
        this.configuration = configuration;
    }

    @Override
    public List<MeterReadingCsvRow> parseFile(MultipartFile file) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return parseFileRows(fileReader);
        } catch (Exception exception) {
            throw new CsvParseException("Can not parse fractions CSV file!", exception);
        }
    }

    private List<MeterReadingCsvRow> parseFileRows(BufferedReader fileReader) throws IOException {
        List<MeterReadingCsvRow> parsedRows = new ArrayList<>();

        boolean shouldSkipFirstRow = configuration.isFirstRowTitle();
        if (shouldSkipFirstRow) {
            fileReader.readLine();
        }

        String row;
        while ((row = fileReader.readLine()) != null) {
            parsedRows.add(parseRow(row));
        }

        return parsedRows;
    }

    private MeterReadingCsvRow parseRow(String row) {
        String[] values = row.split(configuration.getDelimiter());

        String meterPublicId = values[configuration.getMeterIdPosition()].trim();
        String profilePublicId = values[configuration.getProfileIdPosition()].trim();
        Month month = monthMapper.getMonth(values[configuration.getMonthPosition()].trim());
        Double meterReading = Double.valueOf(values[configuration.getMeterReadingPosition()].trim());

        return new MeterReadingCsvRow(meterPublicId, profilePublicId, month, meterReading);
    }
}
