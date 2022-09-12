package com.zaioro.mechanicservice.controller;

import com.zaioro.mechanicservice.dto.request.CarAddRequestDto;
import com.zaioro.mechanicservice.dto.request.ReservationRequestDto;
import com.zaioro.mechanicservice.dto.response.CarResponseDto;
import com.zaioro.mechanicservice.dto.response.ReservationResponseDto;
import com.zaioro.mechanicservice.exceptions.ReservationOperationAlreadyDoneException;
import com.zaioro.mechanicservice.model.serviceType.ServiceType;
import com.zaioro.mechanicservice.model.user.UserStatus;
import com.zaioro.mechanicservice.repository.ServiceTypeRepository;
import com.zaioro.mechanicservice.service.ClientService;
import com.zaioro.mechanicservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("client")
public class ClientController {
    private final ClientService clientService;
    private final UserService userService;
    private final ServiceTypeRepository serviceTypeRepository;

    @PostMapping(path = "/car/{clientId}")
    public String addCar(@Valid @ModelAttribute("car") CarAddRequestDto carAddRequestDto, BindingResult bindingResult, @PathVariable(name = "clientId") Integer clientId, Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("car", carAddRequestDto);
            model.addAttribute("clientId", clientId);
            return "create_car";
        }
        clientService.addCar(carAddRequestDto, clientId);
        ArrayList<CarResponseDto> carsList =  userService.getUsersCars(clientId);
        Collections.sort(carsList, Comparator.comparing(CarResponseDto::getBrand));
        model.addAttribute("cars", carsList);
        return "cars";
    }

    @GetMapping(path = "/addcar/{clientId}")
    public String addCar(@PathVariable(name = "clientId") Integer clientId, Model model) {
        model.addAttribute("car", new CarAddRequestDto());
        model.addAttribute("clientId", clientId);
        return "create_car";
    }

//    @PostMapping(path = "/reservation/{clientId}")
//    public String createReservation(@ModelAttribute ReservationRequestDto reservationRequestDto, @PathVariable(name = "clientId") Integer clientId, @RequestParam(value = "attachment", required = false) MultipartFile multipartFile) throws IOException {
//        return clientService.createReservation(reservationRequestDto, clientId, multipartFile);
//    }

    @PostMapping(path = "/reservation/{clientId}")
    public String createReservation(@Valid @ModelAttribute("reservation") ReservationRequestDto reservationRequestDto, BindingResult bindingResult, @PathVariable(name = "clientId") Integer clientId, @RequestParam(value = "attachment", required = false) MultipartFile multipartFile, Model model) throws IOException {
        if(bindingResult.hasErrors()){
//            model.addAttribute("clientId", clientId);
            ArrayList<CarResponseDto> carsList =  userService.getUsersCars(clientId);
            Collections.sort(carsList, Comparator.comparing(CarResponseDto::getBrand));
            model.addAttribute("cars", carsList);
            model.addAttribute("reservation", reservationRequestDto);
            model.addAttribute("serviceTypes", userService.getAllServiceTypes());
            return "create_reservation";
        }
        clientService.createReservation(reservationRequestDto, clientId, multipartFile);
        ArrayList<ReservationResponseDto> reservationList =  userService.getUserReservations(clientId);
        //Collections.sort(reservationList, Comparator.comparing(ReservationResponseDto::getClientEmail));
        model.addAttribute("clientReservations", reservationList);
//        model.addAttribute("clientId", clientId);
        return "client_reservations";
    }

    @GetMapping(path = "/addreservation/{clientId}")
    public String createReservation(@PathVariable(name = "clientId") Integer clientId, Model model){
        String userStatus = userService.getUserStatus(clientId);
        if(!userStatus.equals(UserStatus.VERIFIED)){
            model.addAttribute("status", userStatus);
            return "cannot_create_reservation";
        }
        model.addAttribute("clientId", clientId);
        ArrayList<CarResponseDto> carsList =  userService.getUsersCars(clientId);
        Collections.sort(carsList, Comparator.comparing(CarResponseDto::getBrand));
        model.addAttribute("cars", carsList);
        model.addAttribute("reservation", new ReservationRequestDto());
        model.addAttribute("serviceTypes", userService.getAllServiceTypes());
        return "create_reservation";
    }

//    //Nie ma w postmanie
//    @PostMapping(path = "/reservationSimple")
//    public String createReservation(@RequestBody ReservationRequestDto reservationRequestDto) throws IOException {
//        return clientService.createReservation(reservationRequestDto, null);
//    }

    //Nie ma w postmanie
    @PostMapping(path = "reservation/{reservationId}/addAttachment/{userId}")
    public String addAttachmentToReservation(@PathVariable(name = "userId") Integer userId, @PathVariable(name = "reservationId") Integer reservationId, @RequestParam(value = "attachment", required = false) MultipartFile multipartFile) throws IOException {
        return clientService.addAttachment(userId, reservationId, multipartFile);
    }

//    @PatchMapping(path = "reservation/{reservationId}/confirm")
//    public String confirmReservation(@PathVariable(name = "reservationId") Integer reservationId, @RequestParam Integer dateId) {
//        return clientService.confirmReservation(reservationId, dateId);
//    }

    @GetMapping("/reservation/confirm/{reservationId}/{clientId}")
    public ModelAndView confirmReservation(@PathVariable(name = "reservationId") Integer reservationId, @PathVariable(name = "clientId") Integer clientId, ModelMap model) {
        try{
            clientService.confirmReservation(reservationId);
            String url = String.format("redirect:/user/reservations/%s", clientId);
            model.addAttribute("Msg", "successfully confirmed");
            return new ModelAndView(url, model);
        }
        catch(ReservationOperationAlreadyDoneException e){
            String url = String.format("redirect:/user/reservations/%s", clientId);
            model.addAttribute("Msg", "already confirmed");
            return new ModelAndView(url, model);
        }
    }

    @GetMapping("/reservation/reject/{reservationId}/{clientId}")
    public ModelAndView rejectReservation(@PathVariable(name = "reservationId") Integer reservationId, @PathVariable(name = "clientId") Integer clientId, ModelMap model) {
        try{
            clientService.rejectReservation(reservationId);
            String url = String.format("redirect:/user/reservations/%s", clientId);
            model.addAttribute("Msg", "successfully rejected");
            return new ModelAndView(url, model);
        }
        catch(ReservationOperationAlreadyDoneException e){
            String url = String.format("redirect:/user/reservations/%s", clientId);
            model.addAttribute("Msg", "already rejected");
            return new ModelAndView(url, model);
        }
    }

    @GetMapping("/reservation/cancel/{reservationId}/{clientId}")
    public ModelAndView cancelReservation(@PathVariable(name = "reservationId") Integer reservationId, @PathVariable(name = "clientId") Integer clientId, ModelMap model) {
        try{
            clientService.cancelReservation(reservationId);
            String url = String.format("redirect:/user/reservations/%s", clientId);
            model.addAttribute("Msg", "successfully canceled");
            return new ModelAndView(url, model);
        }
        catch(ReservationOperationAlreadyDoneException e){
            String url = String.format("redirect:/user/reservations/%s", clientId);
            model.addAttribute("Msg", "already canceled");
            return new ModelAndView(url, model);
        }
    }
}
