package com.energy.consumption.csv.model;

import java.time.Month;

public class MeterReadingCsvRow {

    private final String meterPublicId;
    private final String profilePublicId;
    private Month month;
    private final Double meterValue;

    public MeterReadingCsvRow(String meterPublicId, String profilePublicId, Month month, Double meterValue) {
        this.meterPublicId = meterPublicId;
        this.profilePublicId = profilePublicId;
        this.month = month;
        this.meterValue = meterValue;
    }

    public String getMeterPublicId() {
        return meterPublicId;
    }

    public String getProfilePublicId() {
        return profilePublicId;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Double getMeterValue() {
        return meterValue;
    }
}