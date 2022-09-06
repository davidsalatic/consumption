package com.energy.consumption.fraction.service;

import org.springframework.web.multipart.MultipartFile;

import java.time.Month;

public interface FractionService {
    void processFile(MultipartFile file);

    Double getRatioByProfileIdAndMonth(String profileId, Month month);
}
