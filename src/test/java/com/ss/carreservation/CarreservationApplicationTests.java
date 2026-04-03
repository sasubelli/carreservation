package com.ss.carreservation;

import com.ss.carreservation.entity.Car;
import com.ss.carreservation.entity.Reservation;
import com.ss.carreservation.entity.ReservationStatus;
import com.ss.carreservation.repository.CarRepository;
import com.ss.carreservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest is broken in Spring Boot 4.0.x due to an incomplete Maven Central publish
// (the spring-boot-test-autoconfigure jar is missing the orm.jpa package entirely).
// Workaround: use @SpringBootTest with H2 test properties to get full JPA context
// without needing a real PostgreSQL instance.
// @Transactional rolls back after each test, keeping tests isolated.
@SpringBootTest
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class CarreservationApplicationTests {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private CarRepository carRepository;

	private Car testCar;

	@BeforeEach
	void setUp() {
		testCar = new Car();
		testCar.setPricePerDay(BigDecimal.valueOf(100.0));
		testCar.setActive(true);
		testCar = carRepository.save(testCar);
	}

	@Test
	@DisplayName("Context loads and repositories are wired correctly")
	void contextLoads() {
		assertThat(reservationRepository).isNotNull();
		assertThat(carRepository).isNotNull();
	}

	@Test
	@DisplayName("Should persist and retrieve a reservation by car ID")
	void shouldSaveAndFindReservationByCarId() {
		Reservation reservation = new Reservation();
		reservation.setCar(testCar);
		reservation.setStatus(ReservationStatus.CONFIRMED);
		reservation.setStartDate(LocalDateTime.of(2026, 3, 10, 10, 0));
		reservation.setEndDate(LocalDateTime.of(2026, 3, 15, 10, 0));
		reservationRepository.save(reservation);

		List<Reservation> results = reservationRepository.findByCar_CarId(testCar.getCarId());

		assertThat(results).hasSize(1);
		assertThat(results.get(0).getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
		assertThat(results.get(0).getStartDate()).isEqualTo(LocalDateTime.of(2026, 3, 10, 10, 0));
	}

	@Test
	@DisplayName("Should return empty list when no reservations exist for car")
	void shouldReturnEmptyWhenNoReservations() {
		List<Reservation> results = reservationRepository.findByCar_CarId(testCar.getCarId());

		assertThat(results).isEmpty();
	}

	@Test
	@DisplayName("Should persist car and retrieve by ID")
	void shouldSaveAndFindCar() {
		Car found = carRepository.findById(testCar.getCarId()).orElse(null);

		assertThat(found).isNotNull();
		assertThat(found.getPricePerDay()).isEqualByComparingTo(BigDecimal.valueOf(100.0));
	}

	@Test
	@DisplayName("Should store multiple reservations for same car")
	void shouldSaveMultipleReservationsForSameCar() {
		Reservation r1 = new Reservation();
		r1.setCar(testCar);
		r1.setStatus(ReservationStatus.CONFIRMED);
		r1.setStartDate(LocalDateTime.of(2026, 3, 1, 10, 0));
		r1.setEndDate(LocalDateTime.of(2026, 3, 5, 10, 0));

		Reservation r2 = new Reservation();
		r2.setCar(testCar);
		r2.setStatus(ReservationStatus.PENDING);
		r2.setStartDate(LocalDateTime.of(2026, 3, 10, 10, 0));
		r2.setEndDate(LocalDateTime.of(2026, 3, 15, 10, 0));

		reservationRepository.save(r1);
		reservationRepository.save(r2);

		List<Reservation> results = reservationRepository.findByCar_CarId(testCar.getCarId());

		assertThat(results).hasSize(2);
	}
}