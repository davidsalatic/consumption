package com.energy.consumption.meterreading.repository;

import com.energy.consumption.meterreading.model.MeterReading;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.Month;
import java.util.Optional;

public interface MeterReadingRepository extends CrudRepository<MeterReading, String> {

    @Query("SELECT meterReading " +
            "FROM MeterReading meterReading " +
            "JOIN Meter meter ON meterReading.meter.id = meter.id " +
            "WHERE meterReading.monthOfReading = :month " +
            "AND meter.publicId = :meterPublicId")
    Optional<MeterReading> findByMeterPublicIdAndMonthOfReading(String meterPublicId, Month month);
}
