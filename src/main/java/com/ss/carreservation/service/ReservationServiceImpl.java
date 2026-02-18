package com.ss.carreservation.service;

import com.ss.carreservation.dto.ReservationDTO;
import com.ss.carreservation.entity.Car;
import com.ss.carreservation.entity.Reservation;
import com.ss.carreservation.repository.CarRepository;
import com.ss.carreservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;

    private final CarRepository carRepo;
    private final Set<Long> activeLocks = ConcurrentHashMap.newKeySet();

    @Override
    public Reservation createReservation(Reservation reservation) {
        // Business logic: e.g., calculate total price before saving
        return repository.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return repository.findAll();
    }

    @Override
    public Reservation getReservationById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Reservation getReservationByCarId(Long carId) {
        return repository.findByCarId(carId);
    }

    @Override
    public boolean isAvailable(Long carId, LocalDate start, LocalDate end) {

        List<Reservation> reservations = Collections.singletonList(getReservationByCarId(carId));

        return reservations.stream()
                .filter(res -> List.of("CONFIRMED", "PENDING").contains(res.getStatus()))
                .noneMatch(res -> !(res.getEndDate().isBefore(start.atStartOfDay()) || res.getStartDate().isAfter(end.atStartOfDay())));
    }

    @Override
    public double calculatePrice(Long carId, LocalDate start, LocalDate end) {
        Car car = carRepo.findById(carId).orElseThrow();
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        return car.getPricePerDay() * days;
    }

    @Transactional
    public void reserveCar(ReservationDTO dto) {
        // Simple LockManager Logic
        if (!activeLocks.add(dto.getCarId())) {
            throw new RuntimeException("Resource is locked");
        }

        try {
            if (!isAvailable(dto.getCarId(), dto.getStartDate(), dto.getEndDate())) {
                throw new RuntimeException("Car is already booked");
            }
            // Logic to save reservation...

        } finally {
            activeLocks.remove(dto.getCarId());
        }
    }
}
