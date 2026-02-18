package com.ss.carreservation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDTO {
    private Long carId;
    private LocalDate startDate;
    private LocalDate endDate;
}