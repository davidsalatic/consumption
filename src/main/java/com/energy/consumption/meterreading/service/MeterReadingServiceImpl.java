package com.energy.consumption.meterreading.service;

import com.energy.consumption.csv.model.MeterReadingCsvRow;
import com.energy.consumption.csv.service.MeterReadingCsvParser;
import com.energy.consumption.exception.model.ValidationException;
import com.energy.consumption.meter.model.Meter;
import com.energy.consumption.meter.service.MeterService;
import com.energy.consumption.meterreading.model.MeterReading;
import com.energy.consumption.meterreading.repository.MeterReadingRepository;
import com.energy.consumption.model.EntityWithPublicId;
import com.energy.consumption.profile.model.Profile;
import com.energy.consumption.profile.service.ProfileService;
import com.energy.consumption.util.MapUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MeterReadingServiceImpl implements MeterReadingService {

    private final ProfileService profileService;
    private final MeterService meterService;
    private final MeterReadingValidationService meterReadingValidationService;
    private final MeterReadingCsvParser meterReadingCsvParser;
    private final MeterReadingRepository meterReadingRepository;
    private final MapUtil mapUtil;

    public MeterReadingServiceImpl(ProfileService profileService, MeterService meterService, MeterReadingValidationService meterReadingValidationService, MeterReadingCsvParser meterReadingCsvParser, MeterReadingRepository meterReadingRepository, MapUtil mapUtil) {
        this.profileService = profileService;
        this.meterService = meterService;
        this.meterReadingValidationService = meterReadingValidationService;
        this.meterReadingCsvParser = meterReadingCsvParser;
        this.meterReadingRepository = meterReadingRepository;
        this.mapUtil = mapUtil;
    }

    @Override
    public void processFile(MultipartFile file) {
        List<MeterReadingCsvRow> parsedRows = meterReadingCsvParser.parseFile(file);

        Set<String> profilePublicIds = getProfilePublicIds(parsedRows);
        meterReadingValidationService.validateProfilesExist(profilePublicIds);

        Set<String> invalidPublicMeterIds = meterReadingValidationService.getInvalidMeterPublicIds(parsedRows);
        parsedRows = filterOutInvalidMeters(parsedRows, invalidPublicMeterIds);

        save(parsedRows);
    }

    private List<MeterReadingCsvRow> filterOutInvalidMeters(List<MeterReadingCsvRow> parsedRows, Set<String> invalidPublicMeterIds) {
        return parsedRows.stream()
                .filter(reading -> !invalidPublicMeterIds.contains(reading.getMeterPublicId()))
                .collect(Collectors.toList());
    }

    private void save(List<MeterReadingCsvRow> meterReadingCsvRows) {
        Set<String> meterPublicIds = getMeterPublicIds(meterReadingCsvRows);
        Set<String> profilePublicIds = getProfilePublicIds(meterReadingCsvRows);

        List<Meter> createdMeters = meterService.create(meterPublicIds);
        Map<String, EntityWithPublicId> publicMeterIdMeterPair = mapUtil.groupByPublicId(createdMeters);

        List<Profile> existingProfiles = profileService.getByPublicIds(profilePublicIds);
        Map<String, EntityWithPublicId> publicProfileIdProfilePair = mapUtil.groupByPublicId(existingProfiles);

        List<MeterReading> meterReadingsToSave = new ArrayList<>();

        for (MeterReadingCsvRow meterReadingCsvRow : meterReadingCsvRows) {
            MeterReading meterReading = new MeterReading();
            meterReading.setMonthOfReading(meterReadingCsvRow.getMonth());
            meterReading.setMeterValue(meterReadingCsvRow.getMeterValue());

            EntityWithPublicId meter = publicMeterIdMeterPair.get(meterReadingCsvRow.getMeterPublicId());
            meterReading.setMeter((Meter) meter);

            EntityWithPublicId profile = publicProfileIdProfilePair.get(meterReadingCsvRow.getProfilePublicId());
            meterReading.setProfile((Profile) profile);

            meterReadingsToSave.add(meterReading);
        }

        meterReadingRepository.saveAll(meterReadingsToSave);
    }

    private Set<String> getProfilePublicIds(List<MeterReadingCsvRow> parsedRows) {
        return parsedRows.stream()
                .map(MeterReadingCsvRow::getProfilePublicId)
                .collect(Collectors.toSet());
    }

    private Set<String> getMeterPublicIds(List<MeterReadingCsvRow> parsedRows) {
        return parsedRows.stream()
                .map(MeterReadingCsvRow::getMeterPublicId)
                .collect(Collectors.toSet());
    }

    @Override
    public MeterReading getByMeterPublicIdAndMonth(String meterPublicId, Month month) {
        return meterReadingRepository.findByMeterPublicIdAndMonthOfReading(meterPublicId, month)
                .orElseThrow(() -> new ValidationException("No reading found for meter: " + meterPublicId + " in " + month));
    }
}
