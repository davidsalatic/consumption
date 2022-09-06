package com.energy.consumption.csv.service;

import com.energy.consumption.csv.model.FractionCsvRow;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FractionCsvParser {
    List<FractionCsvRow> parseFile(MultipartFile file);
}
