package com.zaioro.mechanicservice.controller;

import com.zaioro.mechanicservice.util.email.EmailDetails;
import com.zaioro.mechanicservice.util.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("email")
@Slf4j
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendMail")
    public String sendMail(@Valid @ModelAttribute("email") EmailDetails details, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("email", details);
            return "create_email";
        }
        details.setRecipient("sigaretif@gmail.com");
        emailService.sendSimpleMail(details);
        return "index";
    }

    @GetMapping("/newEmail")
    public String newEmail(Model model){
        model.addAttribute("email", new EmailDetails());
        return "create_email";
    }

}
