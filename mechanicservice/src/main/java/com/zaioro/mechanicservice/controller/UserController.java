package com.zaioro.mechanicservice.controller;

import com.zaioro.mechanicservice.dto.response.CarResponseDto;
import com.zaioro.mechanicservice.dto.response.ReservationDateResponseDto;
import com.zaioro.mechanicservice.dto.response.ReservationResponseDto;
import com.zaioro.mechanicservice.dto.response.ServiceTypeResponseDto;
import com.zaioro.mechanicservice.dto.response.UserResponseDto;
import com.zaioro.mechanicservice.exceptions.UserAlreadyBlockedException;
import com.zaioro.mechanicservice.exceptions.UserAlreadyVerifiedException;
import com.zaioro.mechanicservice.model.user.User;
import com.zaioro.mechanicservice.repository.CarRepository;
import com.zaioro.mechanicservice.repository.UserRepository;
import com.zaioro.mechanicservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Controller
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

//    @PatchMapping(path = "/verify/{id}")
//    public String verifyUserById(@PathVariable(name = "id") Integer id) {
//        return userService.verifyUserById(id);
//    }

    @GetMapping(path = "/verify/{id}")
    public String verifyUserById(@PathVariable(name = "id") Integer id, Model model) {
        User user = userRepository.findById(id).get();
        String firstNameAndLastName = user.getFirstName() + " " + user.getLastName();
        try{
            userService.verifyUserById(id);
        }
        catch (UserAlreadyVerifiedException e){
            ArrayList<UserResponseDto> userList =  userService.getAllUsers();
            Collections.sort(userList, Comparator.comparing(UserResponseDto::getEmail));
            model.addAttribute("clients", userList);
            model.addAttribute("errorMessage", "User " + firstNameAndLastName + " already verified!");
            return "clients";
        }
        ArrayList<UserResponseDto> userList =  userService.getAllUsers();
        Collections.sort(userList, Comparator.comparing(UserResponseDto::getEmail));
        model.addAttribute("clients", userList);
        model.addAttribute("successMessage", "User " + firstNameAndLastName + " successfully verified!");
        return "clients";
    }

//    @PatchMapping(path = "/block/{id}")
//    public String blockUserById(@PathVariable(name = "id") Integer id) {
//        return userService.blockUserById(id);
//    }

    @GetMapping(path = "/block/{id}")
    public String blockUserById(@PathVariable(name = "id") Integer id, Model model) {
        User user = userRepository.findById(id).get();
        String firstNameAndLastName = user.getFirstName() + " " + user.getLastName();
        try{
            userService.blockUserById(id);
        }
        catch (UserAlreadyBlockedException e){
            ArrayList<UserResponseDto> userList =  userService.getAllUsers();
            Collections.sort(userList, Comparator.comparing(UserResponseDto::getEmail));
            model.addAttribute("clients", userList);
            model.addAttribute("errorMessage", "User " + firstNameAndLastName + " already blocked!");
            return "clients";
        }
        ArrayList<UserResponseDto> userList =  userService.getAllUsers();
        Collections.sort(userList, Comparator.comparing(UserResponseDto::getEmail));
        model.addAttribute("clients", userList);
        model.addAttribute("successMessage", "User " + firstNameAndLastName + " successfully blocked!");
        return "clients";
    }

    @PatchMapping(path = "/role/{id}/{newRole}")
    public String changeRoleById(@PathVariable(name = "id") Integer id, @PathVariable(name = "newRole") String newRole) {
        return userService.changeRoleById(id, newRole);
    }

//    @DeleteMapping(path = "/delete/{id}")
//    public String deleteUserById(@PathVariable(name = "id") Integer id) {
//        return userService.deleteUserById(id);
//    }

    @GetMapping("/delete/{id}")
    public String deleteUserById(@PathVariable(name = "id") Integer id){
        userService.deleteUserById(id);
        return "redirect:/user/all";
    }

//    @GetMapping(path = "/all")
//    public ArrayList<UserResponseDto> getAllUsers() {
//        return userService.getAllUsers();
//    }


    @GetMapping(path = "/all")
    public String getAllUsers(Model model) {
        ArrayList<UserResponseDto> userList =  userService.getAllUsers();
        Collections.sort(userList, Comparator.comparing(UserResponseDto::getEmail));
        model.addAttribute("clients", userList);
        return "clients";
    }

    @GetMapping(path = "/byName/{name}")
    public ArrayList<UserResponseDto> getUserByName(@PathVariable(name = "name") String[] name) {
        return userService.getUserByName(name[0], name[1]);
    }

//    @GetMapping(path = "/{id}")
//    public UserResponseDto getUserById(@PathVariable(name = "id") Integer id) {
//        return userService.getUserById(id);
//    }

    @GetMapping(path = "/{id}")
    public String getUserById(@PathVariable(name = "id") Integer id, Model model) {
        model.addAttribute("currentUser", userService.getUserById(id));
        return "profile";
    }

    @GetMapping(path = "/cars/{id}")
    public String getUsersCars(@PathVariable(name = "id") Integer id, Model model) {
        ArrayList<CarResponseDto> carsList =  userService.getUsersCars(id);
        Collections.sort(carsList, Comparator.comparing(CarResponseDto::getBrand));
        model.addAttribute("cars", carsList);
        return "cars";
    }

    @GetMapping("/cars/delete/{id}")
    public String deleteCarById(@PathVariable(name = "id") Integer id){
        String userId = userService.deleteCarById(id);
        String url = String.format("redirect:/user/cars/%s", userId);
        return url;
    }


//    @GetMapping(path = "/reservations/{id}")
//    public ArrayList<ReservationResponseDto> getUserReservations(@PathVariable(name = "id") Integer id) {
//
//        return userService.getUserReservations(id);
//    }

    @GetMapping(path = "/reservations/{id}")
    public String getUserReservations(@PathVariable(name = "id") Integer id, Model model, @ModelAttribute("Msg") String msg) {
        ArrayList<ReservationResponseDto> reservationList =  userService.getUserReservations(id);
        //Collections.sort(reservationList, Comparator.comparing(ReservationResponseDto::getClientEmail));
        model.addAttribute("clientReservations", reservationList);
        if(msg.startsWith("already")){
            model.addAttribute("errorMessage", "Reservation " + msg);
        }
        if(msg.startsWith("successfully")){
            model.addAttribute("successMessage", "Reservation " + msg);
        }
        return "client_reservations";
    }

    @GetMapping(path = "/reservations/dates/{id}")
    public ArrayList<ReservationDateResponseDto> getUserReservationDates(@PathVariable(name = "id") Integer reservationId) {
        return userService.getUserReservationDates(reservationId);
    }

    @GetMapping(path = "/serviceTypes")
    public ArrayList<ServiceTypeResponseDto> getAllServiceTypes() {
        return userService.getAllServiceTypes();
    }


}
