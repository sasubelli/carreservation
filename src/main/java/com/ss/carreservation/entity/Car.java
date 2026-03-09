package com.ss.carreservation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
    private BigDecimal pricePerDay;
    private boolean isActive = true;
}