package com.ss.carreservation.dto;

public record CarDTO(
        Long carId,
        String make,
        String model,
        int year,
        String carType,
        String location,
        double pricePerDay
) {
}
