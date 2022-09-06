package com.energy.consumption.consumption.controller;

import com.energy.consumption.consumption.service.ConsumptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;

@RestController
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    @GetMapping("/meters/{meterPublicId}/month/{month}/consumption")
    public ResponseEntity<Double> getConsumption(@PathVariable String meterPublicId, @PathVariable Month month) {
        return new ResponseEntity<>(consumptionService.getConsumption(meterPublicId, month), HttpStatus.OK);
    }
}
