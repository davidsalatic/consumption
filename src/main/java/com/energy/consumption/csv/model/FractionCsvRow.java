package com.energy.consumption.csv.model;

import java.time.Month;

public class FractionCsvRow {

    private final Month month;
    private final Double ratio;
    private final String profilePublicId;

    public FractionCsvRow(Month month, String profilePublicId, Double ratio) {
        this.month = month;
        this.profilePublicId = profilePublicId;
        this.ratio = ratio;
    }

    public Month getMonth() {
        return month;
    }

    public String getProfilePublicId() {
        return profilePublicId;
    }

    public Double getRatio() {
        return ratio;
    }
}
