package com.ss.carreservation.repository;

import com.ss.carreservation.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // You can add custom queries here, e.g., find payments by reservation ID
    Optional<Payment> findByReservationReservationId(Long reservationId);
}
