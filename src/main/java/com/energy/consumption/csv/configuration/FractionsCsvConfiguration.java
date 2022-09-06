package com.energy.consumption.csv.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "fractions-csv-configuration")
public class FractionsCsvConfiguration {

    private int monthPosition;
    private int profileIdPosition;
    private int ratioPosition;
    private boolean firstRowTitle;
    private String delimiter;

    public int getMonthPosition() {
        return monthPosition;
    }

    public void setMonthPosition(int monthPosition) {
        this.monthPosition = monthPosition;
    }

    public int getProfileIdPosition() {
        return profileIdPosition;
    }

    public void setProfileIdPosition(int profileIdPosition) {
        this.profileIdPosition = profileIdPosition;
    }

    public int getRatioPosition() {
        return ratioPosition;
    }

    public void setRatioPosition(int ratioPosition) {
        this.ratioPosition = ratioPosition;
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
