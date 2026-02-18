package com.ss.carreservation.controller;

import com.ss.carreservation.entity.Reservation;
import com.ss.carreservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
      //bad practice need to implement using constructor injection
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
        return ResponseEntity.ok(service.createReservation(reservation));
    }

    @GetMapping
    public List<Reservation> getAll() {
        return service.getAllReservations();
    }
}
