package com.ss.carreservation.dto;

import java.math.BigDecimal;

public record CarDTO(
        Long carId,
        String make,
        String model,
        int year,
        String carType,
        String location,
        BigDecimal pricePerDay
) {
}
