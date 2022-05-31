package com.zaioro.mechanicservice.service;

import com.zaioro.mechanicservice.dto.request.CarAddRequestDto;
import com.zaioro.mechanicservice.model.car.Car;
import com.zaioro.mechanicservice.model.user.User;
import com.zaioro.mechanicservice.repository.CarRepository;
import com.zaioro.mechanicservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public String addCar(CarAddRequestDto carAddRequestDto){
        Optional<User> foundUser = userRepository.findById(carAddRequestDto.getUserId());
        if(foundUser.isPresent()){
            User user = foundUser.get();
            Car car = Car.builder()
                    .brand(carAddRequestDto.getBrand())
                    .model(carAddRequestDto.getModel())
                    .yearOfProduction(carAddRequestDto.getYearOfProduction())
                    .body(carAddRequestDto.getBody())
                    .user(user)
                    .build();
            carRepository.save(car);
            return "Success";
        }
        return "Failure";
    }
}
