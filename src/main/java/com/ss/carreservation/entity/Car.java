package com.ss.carreservation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    private Long carId;
    private String make;
    private String model;
    private int year;
    private String carType;
    private String location;
    private double pricePerDay;
    private boolean isActive = true;
}