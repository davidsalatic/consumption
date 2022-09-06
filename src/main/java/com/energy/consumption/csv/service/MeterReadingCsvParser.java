package com.energy.consumption.csv.service;

import com.energy.consumption.csv.model.MeterReadingCsvRow;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MeterReadingCsvParser {
    List<MeterReadingCsvRow> parseFile(MultipartFile file);
}
