package com.energy.consumption.meter.repository;

import com.energy.consumption.meter.model.Meter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeterRepository extends JpaRepository<Meter, String> {
    Optional<Meter> findByPublicId(String publicId);
}
