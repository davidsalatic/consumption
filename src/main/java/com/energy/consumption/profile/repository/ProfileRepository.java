package com.energy.consumption.profile.repository;

import com.energy.consumption.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProfileRepository extends JpaRepository<Profile, String> {
    List<Profile> findByPublicIdIn(Set<String> externalIds);

    Optional<Profile> findByPublicId(String publicId);
}
