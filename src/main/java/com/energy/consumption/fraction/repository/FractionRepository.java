package com.energy.consumption.fraction.repository;

import com.energy.consumption.fraction.model.Fraction;
import org.springframework.data.repository.CrudRepository;

import java.time.Month;
import java.util.Optional;

public interface FractionRepository extends CrudRepository<Fraction, String> {
    Optional<Fraction> findByProfileIdAndMonthOfFraction(String profileId, Month month);
}
