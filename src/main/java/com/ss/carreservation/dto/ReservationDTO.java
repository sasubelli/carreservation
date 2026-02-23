package com.ss.carreservation.dto;

import com.ss.carreservation.entity.Car;
import com.ss.carreservation.entity.Customer;
import com.ss.carreservation.entity.ReservationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservationDTO(
        Long reservationId,
        Long carId,
        Customer customer,
        Car car,
        LocalDateTime startDate,
        LocalDateTime endDate,
        ReservationStatus status,
        Double totalPrice) {
}