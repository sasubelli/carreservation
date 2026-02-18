package com.ss.carreservation.service;

import com.ss.carreservation.dto.ReservationDTO;
import com.ss.carreservation.entity.Car;
import com.ss.carreservation.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    Reservation createReservation(Reservation reservation);
    List<Reservation> getAllReservations();
    Reservation getReservationById(Long id);
    boolean isAvailable(Long carId, LocalDate start, LocalDate end);
    Reservation getReservationByCarId(Long carId);
    double calculatePrice(Long carId, LocalDate start, LocalDate end);
    void reserveCar(ReservationDTO dto);
}
