package com.energy.consumption.fraction.model;

import com.energy.consumption.profile.model.Profile;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Month;

@Entity
@Table(name = "fraction")
public class Fraction {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "ratio", nullable = false)
    private Double ratio;

    @Column(name = "month_of_fraction", nullable = false)
    @Enumerated(EnumType.STRING)
    private Month monthOfFraction;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public void setMonthOfFraction(Month monthOfFraction) {
        this.monthOfFraction = monthOfFraction;
    }

    public Month getMonthOfFraction() {
        return monthOfFraction;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
