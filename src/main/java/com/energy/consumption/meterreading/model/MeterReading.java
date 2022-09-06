package com.energy.consumption.meterreading.model;

import com.energy.consumption.meter.model.Meter;
import com.energy.consumption.profile.model.Profile;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Month;

@Entity
@Table(name = "meter_reading")
public class MeterReading {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "month_of_reading", nullable = false)
    @Enumerated(EnumType.STRING)
    private Month monthOfReading;

    @Column(name = "meter_value", nullable = false)
    private Double meterValue;

    @ManyToOne
    @JoinColumn(name = "meter_id", nullable = false)
    private Meter meter;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    public void setMonthOfReading(Month monthOfReading) {
        this.monthOfReading = monthOfReading;
    }

    public Double getMeterValue() {
        return meterValue;
    }

    public void setMeterValue(Double meterValue) {
        this.meterValue = meterValue;
    }

    public Month getMonthOfReading() {
        return monthOfReading;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Meter getMeter() {
        return meter;
    }

    public void setMeter(Meter meter) {
        this.meter = meter;
    }
}
