package com.energy.consumption.fraction.service;

import com.energy.consumption.csv.model.FractionCsvRow;
import com.energy.consumption.csv.service.FractionCsvParser;
import com.energy.consumption.exception.model.ValidationException;
import com.energy.consumption.fraction.model.Fraction;
import com.energy.consumption.fraction.repository.FractionRepository;
import com.energy.consumption.model.EntityWithPublicId;
import com.energy.consumption.profile.model.Profile;
import com.energy.consumption.profile.service.ProfileService;
import com.energy.consumption.util.MapUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FractionServiceImpl implements FractionService {

    private final ProfileService profileService;
    private final FractionValidationService fractionValidationService;
    private final FractionCsvParser fractionCsvParser;
    private final FractionRepository fractionRepository;
    private final MapUtil mapUtil;

    public FractionServiceImpl(ProfileService profileService, FractionValidationService fractionValidationService, FractionCsvParser fractionCsvParser, FractionRepository fractionRepository, MapUtil mapUtil) {
        this.profileService = profileService;
        this.fractionValidationService = fractionValidationService;
        this.fractionCsvParser = fractionCsvParser;
        this.fractionRepository = fractionRepository;
        this.mapUtil = mapUtil;
    }

    @Transactional
    @Override
    public void processFile(MultipartFile file) {
        List<FractionCsvRow> parsedRows = fractionCsvParser.parseFile(file);

        fractionValidationService.validateFractionsHaveRequiredSum(parsedRows);

        createFractions(parsedRows);
    }

    private void createFractions(List<FractionCsvRow> fractions) {
        List<Fraction> fractionsToSave = new ArrayList<>();

        Set<String> profilePublicIds = fractions.stream()
                .map(FractionCsvRow::getProfilePublicId)
                .collect(Collectors.toSet());

        List<Profile> createdProfiles = profileService.createProfiles(profilePublicIds);
        Map<String, EntityWithPublicId> publicIdProfilePair = mapUtil.groupByPublicId(createdProfiles);

        for (FractionCsvRow fractionCsvRow : fractions) {
            Fraction fraction = new Fraction();
            fraction.setMonthOfFraction(fractionCsvRow.getMonth());
            fraction.setRatio(fractionCsvRow.getRatio());

            EntityWithPublicId profile = publicIdProfilePair.get(fractionCsvRow.getProfilePublicId());
            fraction.setProfile((Profile) profile);

            fractionsToSave.add(fraction);
        }

        fractionRepository.saveAll(fractionsToSave);
    }

    public Double getRatioByProfileIdAndMonth(String profileId, Month month) {
        return fractionRepository.findByProfileIdAndMonthOfFraction(profileId, month)
                .map(Fraction::getRatio)
                .orElseThrow(() -> new ValidationException("No fraction found by profile id: " + profileId + " and month: " + month));
    }
}
