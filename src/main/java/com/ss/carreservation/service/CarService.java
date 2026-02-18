package com.ss.carreservation.service;

import com.ss.carreservation.dto.CarDTO;
import com.ss.carreservation.entity.Car;

import java.util.List;

public interface CarService {
    CarDTO getCarById(Long id);
    List<CarDTO> getAllCars();
    CarDTO createCar(Car car);
}
