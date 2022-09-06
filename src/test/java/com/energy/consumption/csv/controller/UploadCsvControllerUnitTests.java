package com.energy.consumption.csv.controller;

import com.energy.consumption.fraction.service.FractionService;
import com.energy.consumption.meterreading.service.MeterReadingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.energy.consumption.util.TestConstants.FILE_NAME;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UploadCsvControllerUnitTests {

    @Mock
    private FractionService fractionService;
    @Mock
    private MeterReadingService meterReadingService;

    @InjectMocks
    private UploadCsvController uploadCsvController;

    @Test
    void parseProfilesAndFractionsFile_successfully() throws IOException {
        MultipartFile file = getMockFile();

        uploadCsvController.parseProfilesAndFractionsFile(file);

        verify(fractionService).processFile(file);
    }

    @Test
    void parseMeterReadingsFile_successfully() throws IOException {
        MultipartFile file = getMockFile();

        uploadCsvController.parseMeterReadingsFile(file);

        verify(meterReadingService).processFile(file);
    }

    private MultipartFile getMockFile() throws IOException {
        return new MockMultipartFile(FILE_NAME, InputStream.nullInputStream());
    }
}
