package com.zaioro.mechanicservice.repository;

import com.zaioro.mechanicservice.model.car.Car;
import com.zaioro.mechanicservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Integer> {
    Optional<Car> findByBrand(String brand);

    Optional<Car> findByUser(User user);
}
