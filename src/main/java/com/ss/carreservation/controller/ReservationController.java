package com.ss.carreservation.controller;

import com.ss.carreservation.dto.ReservationDTO;
import com.ss.carreservation.entity.Reservation;
import com.ss.carreservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService service) {
        this.reservationService = service;
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> reserveCar(@RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.reserveCar(reservation));
    }

    @GetMapping
    public List<ReservationDTO> getAll() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        ReservationDTO reservationDTO = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservationDTO);
    }

    @GetMapping("/car/{id}")
    ResponseEntity<ReservationDTO> getReservationByCarId(@PathVariable Long id) {
        ReservationDTO reservationDTO = reservationService.getReservationByCarId(id);
        return ResponseEntity.ok(reservationDTO);
    }

    @GetMapping("/caravailable/{id}/{startdate}/{enddate}")
    boolean isAvailable(@PathVariable Long id, @RequestParam LocalDateTime startdate, @RequestParam LocalDateTime enddate) {
        return reservationService.isAvailable(id, startdate, enddate);
    }

    @GetMapping("/price/{id}/{startdate}/{enddate}")
    double calculatePrice(@PathVariable Long id, @RequestParam LocalDateTime startdate, @RequestParam LocalDateTime enddate) {
        return reservationService.calculatePrice(id, startdate, enddate);
    }

}
