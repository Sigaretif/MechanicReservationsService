package com.zaioro.mechanicservice.controller;

import com.zaioro.mechanicservice.dto.request.CarAddRequestDto;
import com.zaioro.mechanicservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("client")
public class ClientController {
    private final ClientService clientService;

    @PostMapping(path = "/car")
    public String addCar(@RequestBody CarAddRequestDto carAddRequestDto){
        return clientService.addCar(carAddRequestDto);
    }
}
