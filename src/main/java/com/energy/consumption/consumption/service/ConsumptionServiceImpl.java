package com.energy.consumption.consumption.service;

import com.energy.consumption.meterreading.model.MeterReading;
import com.energy.consumption.meterreading.service.MeterReadingService;
import org.springframework.stereotype.Service;

import java.time.Month;

@Service
public class ConsumptionServiceImpl implements ConsumptionService {

    private final MeterReadingService meterReadingService;

    public ConsumptionServiceImpl(MeterReadingService meterReadingService) {
        this.meterReadingService = meterReadingService;
    }

    @Override
    public Double getConsumption(String meterPublicId, Month month) {
        MeterReading meterReading = meterReadingService.getByMeterPublicIdAndMonth(meterPublicId, month);

        if (month == Month.JANUARY) {
            return meterReading.getMeterValue();
        }

        Month previousMonth = Month.of(month.getValue() - 1);
        MeterReading previousMonthReading = meterReadingService.getByMeterPublicIdAndMonth(meterPublicId, previousMonth);

        return meterReading.getMeterValue() - previousMonthReading.getMeterValue();
    }
}
