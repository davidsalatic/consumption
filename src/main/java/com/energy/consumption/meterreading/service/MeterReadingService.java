package com.energy.consumption.meterreading.service;

import com.energy.consumption.meterreading.model.MeterReading;
import org.springframework.web.multipart.MultipartFile;

import java.time.Month;

public interface MeterReadingService {
    MeterReading getByMeterPublicIdAndMonth(String meterPublicId, Month month);

    void processFile(MultipartFile file);
}
