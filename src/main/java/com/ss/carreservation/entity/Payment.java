package com.ss.carreservation.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne // Or @ManyToOne depending on if a reservation can have multiple payments
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private Double amount;
    private String method;
    private LocalDateTime date;
    private String status = "FAILED"; // Default status
}