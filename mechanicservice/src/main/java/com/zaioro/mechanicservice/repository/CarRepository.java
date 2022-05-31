package com.zaioro.mechanicservice.repository;

import com.zaioro.mechanicservice.model.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Integer> {
}
