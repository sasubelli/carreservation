package com.ss.carreservation.service;

import com.ss.carreservation.dto.ReservationDTO;
import com.ss.carreservation.entity.Car;
import com.ss.carreservation.entity.Reservation;
import com.ss.carreservation.entity.ReservationStatus;
import com.ss.carreservation.repository.CarRepository;
import com.ss.carreservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;

    private final CarRepository carRepo;
    private final Set<Long> activeLocks = ConcurrentHashMap.newKeySet();


    @Override
    public List<ReservationDTO> getAllReservations() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationDTO getReservationById(Long id) {
        return repository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));

    }

    @Override
    public ReservationDTO getReservationByCarId(Long carId) {
        return repository.findByCarId(carId).stream()
                .findFirst() // or .max(Comparator.comparing(Reservation::getStartDate))
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("No reservations found for carId: " + carId));
    }


    @Override
    public boolean isAvailable(Long carId, LocalDateTime start, LocalDateTime end) {
        List<Reservation> reservations = repository.findByCarId(carId);
        return reservations.stream()
                .filter(res -> res.getStatus() == ReservationStatus.CONFIRMED ||
                        res.getStatus() == ReservationStatus.PENDING)
                .noneMatch(res -> res.getStartDate().isBefore(end) &&
                        res.getEndDate().isAfter(start));
    }

    @Override
    public double calculatePrice(Long carId, LocalDateTime start, LocalDateTime end) {
        Car car = carRepo.findById(carId).orElseThrow();
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        return car.getPricePerDay() * days;
    }

    @Transactional
    public ReservationDTO reserveCar(Reservation reservation) {
        // Simple LockManager Logic
        if (!activeLocks.add(reservation.getCar().getCarId())) {
            throw new RuntimeException("Resource is locked");
        }

        try {
            if (!isAvailable(reservation.getCar().getCarId(), reservation.getStartDate(), reservation.getEndDate())) {
                throw new RuntimeException("Car is already booked");
            }
            // Logic to save reservation...
            repository.saveAndFlush(reservation);

        } finally {
            activeLocks.remove(reservation.getCar().getCarId());
        }
        return this.convertToDTO(reservation);
    }

    // Helper method to keep code --DRY
    private ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getCar().getCarId(),
                reservation.getCustomer(),
                reservation.getCar(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getStatus(),
                reservation.getTotalPrice()
        );
    }
}
