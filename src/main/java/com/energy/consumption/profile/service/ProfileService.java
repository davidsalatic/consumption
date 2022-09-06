package com.energy.consumption.profile.service;

import com.energy.consumption.profile.model.Profile;

import java.util.List;
import java.util.Set;

public interface ProfileService {
    List<Profile> createProfiles(Set<String> publicIds);

    List<Profile> getByPublicIds(Set<String> publicIds);

    Profile getByPublicId(String publicId);
}
