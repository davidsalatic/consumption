package com.energy.consumption.profile.service;

import com.energy.consumption.exception.model.ValidationException;
import com.energy.consumption.profile.model.Profile;
import com.energy.consumption.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public List<Profile> createProfiles(Set<String> publicIds) {
        List<Profile> profilesToCreate = publicIds.stream()
                .map(this::toProfile)
                .collect(Collectors.toList());

        return profileRepository.saveAll(profilesToCreate);
    }

    private Profile toProfile(String publicId) {
        Profile profileToCreate = new Profile();
        profileToCreate.setPublicId(publicId);

        return profileToCreate;
    }

    @Override
    public List<Profile> getByPublicIds(Set<String> publicIds) {
        return profileRepository.findByPublicIdIn(publicIds);
    }

    @Override
    public Profile getByPublicId(String publicId) {
        return profileRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ValidationException("No profile by public id: " + publicId));
    }
}
