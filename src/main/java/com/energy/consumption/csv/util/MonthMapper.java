package com.energy.consumption.csv.util;

import com.energy.consumption.csv.configuration.MonthCsvConfiguration;
import com.energy.consumption.exception.model.ValidationException;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@Component
public class MonthMapper {

    private final Map<String, Month> monthMap;

    public MonthMapper(MonthCsvConfiguration configuration) {
        monthMap = new HashMap<>() {{
            put(configuration.getJanuary(), Month.JANUARY);
            put(configuration.getFebruary(), Month.FEBRUARY);
            put(configuration.getMarch(), Month.MARCH);
            put(configuration.getApril(), Month.APRIL);
            put(configuration.getMay(), Month.MAY);
            put(configuration.getJune(), Month.JUNE);
            put(configuration.getJuly(), Month.JULY);
            put(configuration.getAugust(), Month.AUGUST);
            put(configuration.getSeptember(), Month.SEPTEMBER);
            put(configuration.getOctober(), Month.OCTOBER);
            put(configuration.getNovember(), Month.NOVEMBER);
            put(configuration.getDecember(), Month.DECEMBER);
        }};
    }

    public Month getMonth(String customMonth) {
        Month month = monthMap.get(customMonth);
        if (month == null) {
            throw new ValidationException("No mapping found for month: " + customMonth);
        }
        return month;
    }
}
