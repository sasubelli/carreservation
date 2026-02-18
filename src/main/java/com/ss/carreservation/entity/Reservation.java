package com.ss.carreservation.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    private Long carId;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Car car;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status = "PENDING";
    private Double totalPrice;
}
