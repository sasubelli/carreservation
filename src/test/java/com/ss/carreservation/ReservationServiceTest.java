package com.ss.carreservation;

import com.ss.carreservation.entity.Car;
import com.ss.carreservation.entity.Reservation;
import com.ss.carreservation.entity.ReservationStatus;
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

import java.time.LocalDateTime;
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
        existingBooking.setCar(testCar);
        existingBooking.setStatus(ReservationStatus.CONFIRMED);
        // Setting time to 10:00 AM for existing booking
        existingBooking.setStartDate(LocalDateTime.of(2026, 3, 10, 10, 0));
        existingBooking.setEndDate(LocalDateTime.of(2026, 3, 15, 10, 0));
    }

    @Test
    @DisplayName("Should return true when car has no reservations")
    void testIsAvailable_NoReservations() {
        when(repository.findByCarId(1L)).thenReturn(Collections.emptyList());

        boolean result = reservationService.isAvailable(1L,
                LocalDateTime.of(2026, 3, 1, 10, 0),
                LocalDateTime.of(2026, 3, 5, 10, 0));

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when dates overlap exactly")
    void testIsAvailable_ExactOverlap() {
        when(repository.findByCarId(1L)).thenReturn(List.of(existingBooking));

        boolean result = reservationService.isAvailable(1L,
                LocalDateTime.of(2026, 3, 10, 10, 0),
                LocalDateTime.of(2026, 3, 15, 10, 0));

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when new booking ends before existing starts")
    void testIsAvailable_BeforeExisting() {
        when(repository.findByCarId(1L)).thenReturn(List.of(existingBooking));

        boolean result = reservationService.isAvailable(1L,
                LocalDateTime.of(2026, 3, 1, 10, 0),
                LocalDateTime.of(2026, 3, 9, 10, 0));

        assertTrue(result);
    }

    @Test
    @DisplayName("Should calculate correct price for 3 day booking")
    void testCalculatePrice() {
        when(carRepo.findById(1L)).thenReturn(Optional.of(testCar));

        // Note: Logic depends on if your service uses ChronoUnit.DAYS between start/end
        double price = reservationService.calculatePrice(1L,
                LocalDateTime.of(2026, 3, 1, 10, 0),
                LocalDateTime.of(2026, 3, 3, 10, 0));

        assertEquals(300.0, price);
    }

    @Test
    @DisplayName("Should throw exception when car is already booked")
    void testReserveCar_Conflict() {
        Reservation reservation = new Reservation();
        reservation.setCar(testCar);
        reservation.setStartDate(LocalDateTime.of(2026, 3, 1, 10, 0));
        reservation.setEndDate(LocalDateTime.of(2026, 3, 15, 10, 0));

        when(repository.findByCarId(1L)).thenReturn(List.of(existingBooking));

        assertThrows(RuntimeException.class, () -> reservationService.reserveCar(reservation));
    }

//    @Test
//    @DisplayName("Should save reservation when valid")
//    void testReserveCar_Success() {
//        Reservation reservation = new Reservation();
//        reservation.setCar(testCar);
//        reservation.setStartDate(LocalDateTime.of(2026, 3, 1, 10, 0));
//        reservation.setEndDate(LocalDateTime.of(2026, 3, 15, 10, 0));
//
//        when(repository.findByCarId(1L)).thenReturn(Collections.emptyList());
//        when(carRepo.findById(1L)).thenReturn(Optional.of(testCar));
//
//        reservationService.reserveCar(reservation);
//
//        verify(repository, times(1)).save(any(Reservation.class));
//    }
@Test
@DisplayName("Should save reservation when valid")
void testReserveCar_Success() {

    Long carId = 1L;
    testCar.setCarId(carId);

    Reservation reservation = new Reservation();
    reservation.setCar(testCar);
    reservation.setStartDate(LocalDateTime.of(2026, 3, 1, 10, 0));
    reservation.setEndDate(LocalDateTime.of(2026, 3, 15, 10, 0));

    when(repository.findByCarId(carId)).thenReturn(Collections.emptyList());
    when(carRepo.findById(carId)).thenReturn(Optional.of(testCar));
    when(repository.save(any(Reservation.class))).thenReturn(reservation);

    reservationService.reserveCar(reservation);
    verify(repository, times(1)).save(any(Reservation.class));
}
}