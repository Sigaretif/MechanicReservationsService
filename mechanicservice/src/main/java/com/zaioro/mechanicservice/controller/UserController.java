package com.zaioro.mechanicservice.controller;

import com.zaioro.mechanicservice.dto.response.UserResponseDto;
import com.zaioro.mechanicservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @PatchMapping(path = "/verify/{id}")
    public String verifyUserById(@PathVariable(name = "id") Integer id){
        return userService.verifyUserById(id);
    }

    @PatchMapping(path = "/block/{id}")
    public String blockUserById(@PathVariable(name = "id") Integer id){
        return userService.blockUserById(id);
    }

    @PatchMapping(path = "/role/{id}/{newRole}")
    public String changeRoleById(@PathVariable(name = "id") Integer id, @PathVariable(name = "newRole") String newRole){
        return userService.changeRoleById(id, newRole);
    }

    @DeleteMapping(path = "/delete/{id}")
    public String deleteUserById(@PathVariable(name = "id") Integer id){
        return userService.deleteUserById(id);
    }

    @GetMapping(path = "/all")
    public ArrayList<UserResponseDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping(path = "/{name}")
    public ArrayList<UserResponseDto> getUserByName(@PathVariable(name = "name") String[] name){
        return userService.getUserByName(name[0], name[1]);
    }
}
