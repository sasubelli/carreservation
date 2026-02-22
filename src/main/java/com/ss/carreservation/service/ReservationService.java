package com.ss.carreservation.service;

import com.ss.carreservation.dto.ReservationDTO;
import com.ss.carreservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    List<ReservationDTO> getAllReservations();

    ReservationDTO getReservationById(Long id);

    boolean isAvailable(Long carId, LocalDateTime start, LocalDateTime end);

    ReservationDTO getReservationByCarId(Long carId);

    double calculatePrice(Long carId, LocalDateTime start, LocalDateTime end);

    ReservationDTO reserveCar(Reservation dto);
}
