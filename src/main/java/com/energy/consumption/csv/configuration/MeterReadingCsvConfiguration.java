package com.energy.consumption.csv.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "meter-reading-csv-configuration")
public class MeterReadingCsvConfiguration {

    private int meterIdPosition;
    private int profileIdPosition;
    private int monthPosition;
    private int meterReadingPosition;
    private boolean firstRowTitle;
    private String delimiter;

    public int getMeterIdPosition() {
        return meterIdPosition;
    }

    public void setMeterIdPosition(int meterIdPosition) {
        this.meterIdPosition = meterIdPosition;
    }

    public int getProfileIdPosition() {
        return profileIdPosition;
    }

    public void setProfileIdPosition(int profileIdPosition) {
        this.profileIdPosition = profileIdPosition;
    }

    public int getMonthPosition() {
        return monthPosition;
    }

    public void setMonthPosition(int monthPosition) {
        this.monthPosition = monthPosition;
    }

    public int getMeterReadingPosition() {
        return meterReadingPosition;
    }

    public void setMeterReadingPosition(int meterReadingPosition) {
        this.meterReadingPosition = meterReadingPosition;
    }

    public boolean isFirstRowTitle() {
        return firstRowTitle;
    }

    public void setFirstRowTitle(boolean firstRowTitle) {
        this.firstRowTitle = firstRowTitle;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
