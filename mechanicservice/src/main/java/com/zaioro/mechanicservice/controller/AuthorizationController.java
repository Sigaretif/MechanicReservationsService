package com.zaioro.mechanicservice.controller;

import com.zaioro.mechanicservice.dto.request.UserLoginRequestDto;
import com.zaioro.mechanicservice.dto.request.UserRegistrationDto;
import com.zaioro.mechanicservice.dto.response.UserLoginResponseDto;
import com.zaioro.mechanicservice.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("authorization")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping(path = "/register")
    public String registerUser(@RequestBody UserRegistrationDto userRegistrationDto){
        return authorizationService.registerUser(userRegistrationDto);
    }

    @PostMapping(path = "/login")
    public UserLoginResponseDto loginUser(@RequestBody UserLoginRequestDto userLoginRequestDto){
        return authorizationService.loginUser(userLoginRequestDto);
    }

}
