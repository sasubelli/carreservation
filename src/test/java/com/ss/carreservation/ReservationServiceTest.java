package com.ss.carreservation;

import com.ss.carreservation.dto.ReservationDTO;
import com.ss.carreservation.entity.Car;
import com.ss.carreservation.entity.Reservation;
import com.ss.carreservation.repository.CarRepository;
import com.ss.carreservation.repository.ReservationRepository;
import com.ss.carreservation.service.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository repository;
    @Mock
    private CarRepository carRepo;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Car testCar;
    private Reservation existingBooking;

    @BeforeEach
    void setUp() {
        testCar = new Car();
        testCar.setCarId(1L);
        testCar.setPricePerDay(100.0);

        existingBooking = new Reservation();
        existingBooking.setCarId(1L);
        existingBooking.setStatus("CONFIRMED");
        existingBooking.setStartDate(LocalDate.of(2026, 3, 10).atStartOfDay());
        existingBooking.setEndDate(LocalDate.of(2026, 3, 15).atStartOfDay());
    }

    // --- AVAILABILITY TESTS ---

    @Test
    @DisplayName("Should return true when car has no reservations")
    void testIsAvailable_NoReservations() {
        when(repository.findByCarId(1L)).thenReturn((Reservation) Collections.emptyList());

        boolean result = reservationService.isAvailable(1L, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 5));

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when dates overlap exactly with existing booking")
    void testIsAvailable_ExactOverlap() {
        when(repository.findByCarId(1L)).thenReturn((Reservation) List.of(existingBooking));

        boolean result = reservationService.isAvailable(1L, LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 15));

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when new booking ends before existing starts")
    void testIsAvailable_BeforeExisting() {
        when(repository.findByCarId(1L)).thenReturn((Reservation) List.of(existingBooking));

        boolean result = reservationService.isAvailable(1L, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 9));

        assertTrue(result);
    }

    // --- PRICING TESTS ---

    @Test
    @DisplayName("Should calculate correct price for 3 day booking")
    void testCalculatePrice() {
        when(carRepo.findById(1L)).thenReturn(Optional.of(testCar));

        double price = reservationService.calculatePrice(1L, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 3));

        assertEquals(300.0, price); // (3 - 1) + 1 = 3 days * 100
    }

    // --- LOCKING & RESERVATION TESTS ---

    @Test
    @DisplayName("Should throw exception when car is already booked")
    void testReserveCar_Conflict() {
        ReservationDTO dto = new ReservationDTO();
        dto.setCarId(1L);
        dto.setStartDate(LocalDate.of(2026, 3, 12));
        dto.setEndDate(LocalDate.of(2026, 3, 14));

        when(repository.findByCarId(1L)).thenReturn((Reservation) List.of(existingBooking));

        assertThrows(RuntimeException.class, () -> reservationService.reserveCar(dto));
    }

    @Test
    @DisplayName("Should save reservation when valid")
    void testReserveCar_Success() {
        ReservationDTO dto = new ReservationDTO();
        dto.setCarId(1L);
        dto.setStartDate(LocalDate.of(2026, 4, 1));
        dto.setEndDate(LocalDate.of(2026, 4, 5));

        when(repository.findByCarId(1L)).thenReturn((Reservation) Collections.emptyList());

        reservationService.reserveCar(dto);

        verify(repository, times(1)).save(any(Reservation.class));
    }
}