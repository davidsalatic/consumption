package com.energy.consumption.profile;

import com.energy.consumption.exception.model.ValidationException;
import com.energy.consumption.profile.model.Profile;
import com.energy.consumption.profile.repository.ProfileRepository;
import com.energy.consumption.profile.service.ProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.energy.consumption.util.TestConstants.PROFILE_PUBLIC_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceUnitTests {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Captor
    private ArgumentCaptor<List<Profile>> profilesCaptor;

    @Test
    void createProfiles_successfully() {
        List<Profile> expectedProfiles = new ArrayList<>();

        when(profileRepository.saveAll(profilesCaptor.capture())).thenReturn(expectedProfiles);

        List<Profile> actualProfiles = profileService.createProfiles(Set.of(PROFILE_PUBLIC_ID));

        assertEquals(expectedProfiles, actualProfiles);

        List<Profile> capturedProfiles = profilesCaptor.getValue();
        assertEquals(1, capturedProfiles.size());

        Profile capturedProfile = capturedProfiles.get(0);
        assertEquals(PROFILE_PUBLIC_ID, capturedProfile.getPublicId());
    }

    @Test
    void getByPublicIds_successfully() {
        List<Profile> expectedProfiles = new ArrayList<>();

        when(profileRepository.findByPublicIdIn(Set.of(PROFILE_PUBLIC_ID))).thenReturn(expectedProfiles);

        List<Profile> actualProfiles = profileService.getByPublicIds(Set.of(PROFILE_PUBLIC_ID));

        assertEquals(expectedProfiles, actualProfiles);
    }

    @Test
    void getByPublicId_profileDoesNotExist_throwException() {
        when(profileRepository.findByPublicId(PROFILE_PUBLIC_ID)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> profileService.getByPublicId(PROFILE_PUBLIC_ID));
    }

    @Test
    void getByPublicId_successfully() {
        Profile expectedProfile = new Profile();

        when(profileRepository.findByPublicId(PROFILE_PUBLIC_ID)).thenReturn(Optional.of(expectedProfile));

        Profile actualProfile = profileService.getByPublicId(PROFILE_PUBLIC_ID);

        assertEquals(expectedProfile, actualProfile);
    }
}
