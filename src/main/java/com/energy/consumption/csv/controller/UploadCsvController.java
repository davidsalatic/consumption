package com.energy.consumption.csv.controller;

import com.energy.consumption.fraction.service.FractionService;
import com.energy.consumption.meterreading.service.MeterReadingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/csv")
public class UploadCsvController {

    private final FractionService fractionService;
    private final MeterReadingService meterReadingService;

    public UploadCsvController(FractionService fractionService, MeterReadingService meterReadingService) {
        this.fractionService = fractionService;
        this.meterReadingService = meterReadingService;
    }

    @PostMapping("/upload-profiles-and-fractions")
    public void parseProfilesAndFractionsFile(@RequestParam MultipartFile file) {
        fractionService.processFile(file);
    }

    @PostMapping("/upload-meter-readings")
    public void parseMeterReadingsFile(@RequestParam MultipartFile file) {
        meterReadingService.processFile(file);
    }
}
