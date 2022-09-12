package com.zaioro.mechanicservice.controller;

import com.zaioro.mechanicservice.model.user.User;
import com.zaioro.mechanicservice.service.ClientService;
import com.zaioro.mechanicservice.service.MechanicService;
import com.zaioro.mechanicservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    @GetMapping("index")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }
}
