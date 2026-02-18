package com.ss.carreservation.service;

import com.ss.carreservation.dto.CarDTO;
import com.ss.carreservation.entity.Car;
import com.ss.carreservation.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Automatically injects the CarRepository
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Override
    public CarDTO getCarById(Long id) {
        // Find entity by ID or throw an exception if not found
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
        return mapToDTO(car);
    }

    @Override
    public List<CarDTO> getAllCars() {
        // Fetch all entities and map them to a list of DTOs
        return carRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CarDTO createCar(Car car) {
        Car savedCar = carRepository.save(car);
        return mapToDTO(savedCar);
    }

    // --- Helper Mapping Methods ---

    private CarDTO mapToDTO(Car car) {
        return new CarDTO(
                car.getCarId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getCarType(),
                car.getLocation(),
                car.getPricePerDay()
        );
    }

}