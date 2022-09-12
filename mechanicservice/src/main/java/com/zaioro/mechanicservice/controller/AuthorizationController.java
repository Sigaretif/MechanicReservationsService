package com.zaioro.mechanicservice.controller;

import com.zaioro.mechanicservice.dto.request.UserLoginRequestDto;
import com.zaioro.mechanicservice.dto.request.UserRegistrationDto;
import com.zaioro.mechanicservice.dto.response.UserLoginResponseDto;
import com.zaioro.mechanicservice.exceptions.AccountAwaitingException;
import com.zaioro.mechanicservice.exceptions.UserAlreadyExistsException;
import com.zaioro.mechanicservice.service.AuthorizationService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("authorization")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

//    @PostMapping(path = "/register")
//    public UserResponseDto registerUser(@RequestBody UserRegistrationDto userRegistrationDto){
//        return authorizationService.registerUser(userRegistrationDto);
//    }

    @PostMapping(path = "/register")
    public String registerNewUser(@Valid @ModelAttribute("client") UserRegistrationDto userRegistrationDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            //model.addAttribute("client", userRegistrationDto);
            return "create_user";
        }
        try{
            authorizationService.registerUser(userRegistrationDto);
        }
        catch (UserAlreadyExistsException e){
            model.addAttribute("errorMessage", "User with specified email already exists!");
            return "create_user";
        }
        catch (AccountAwaitingException e){
            model.addAttribute("errorMessage", "Your account is already waiting for approval!");
            return "create_user";
        }
        return "registration_successful";
    }

    @GetMapping(path = "/register")
    public String registerUser(Model model){
        model.addAttribute("client", new UserRegistrationDto());
        return "create_user";
    }

    @PostMapping(path = "/login")
    public UserLoginResponseDto loginUser(@RequestBody UserLoginRequestDto userLoginRequestDto){
        return authorizationService.loginUser(userLoginRequestDto);
    }

//    @GetMapping("/login")
//    public String viewLoginPage() {
//        return "login";
//    }

}
