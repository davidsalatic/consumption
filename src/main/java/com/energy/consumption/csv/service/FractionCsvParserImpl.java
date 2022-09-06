package com.energy.consumption.csv.service;

import com.energy.consumption.csv.configuration.FractionsCsvConfiguration;
import com.energy.consumption.csv.model.FractionCsvRow;
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
public class FractionCsvParserImpl implements FractionCsvParser {

    private final MonthMapper monthMapper;
    private final FractionsCsvConfiguration configuration;

    public FractionCsvParserImpl(MonthMapper monthMapper, FractionsCsvConfiguration configuration) {
        this.monthMapper = monthMapper;
        this.configuration = configuration;
    }

    @Override
    public List<FractionCsvRow> parseFile(MultipartFile file) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return parseFileRows(fileReader);
        } catch (Exception exception) {
            throw new CsvParseException("Can not parse fractions CSV file!", exception);
        }
    }

    private List<FractionCsvRow> parseFileRows(BufferedReader fileReader) throws IOException {
        List<FractionCsvRow> parsedRows = new ArrayList<>();

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

    private FractionCsvRow parseRow(String row) {
        String[] values = row.split(configuration.getDelimiter());

        Month month = monthMapper.getMonth(values[configuration.getMonthPosition()].trim());
        String profilePublicId = values[configuration.getProfileIdPosition()].trim();
        Double ratio = Double.valueOf(values[configuration.getRatioPosition()].trim());

        return new FractionCsvRow(month, profilePublicId, ratio);
    }
}
