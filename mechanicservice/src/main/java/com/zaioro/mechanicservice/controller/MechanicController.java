package com.zaioro.mechanicservice.controller;

import com.zaioro.mechanicservice.dto.request.ReservationDateAndTimeRequest;
import com.zaioro.mechanicservice.dto.request.ReservationDateRequestDto;
import com.zaioro.mechanicservice.dto.response.ReservationResponseDto;
import com.zaioro.mechanicservice.dto.response.UserResponseDto;
import com.zaioro.mechanicservice.exceptions.ReservationAlreadyCompletedException;
import com.zaioro.mechanicservice.exceptions.WrongReservationDtoException;
import com.zaioro.mechanicservice.service.MechanicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("mechanic")
public class MechanicController {
    private final MechanicService mechanicService;

//    @PostMapping(path = "/addReservationDates/{id}")
//    public String proposeReservationDatesForReservation(@RequestBody ArrayList<ReservationDateRequestDto> dates, @PathVariable(name = "id") Integer reservationId){
//        return mechanicService.proposeReservationDatesForReservation(reservationId, dates);
//    }

//    @PostMapping(path = "/addReservationDates/{id}")
//    public String proposeReservationDatesForReservation(@RequestBody ReservationDateRequestDto date, @PathVariable(name = "id") Integer reservationId){
//        return mechanicService.proposeReservationDatesForReservation(reservationId, date);
//    }

    @GetMapping(path = "/addReservationDates/{id}")
    public String addReservationDate(@PathVariable(name = "id") Integer reservationId, Model model){
        model.addAttribute("dateAndCost", new ReservationDateAndTimeRequest());
        model.addAttribute("reservationId", reservationId);
        return "create_date";
    }

    @PostMapping(path = "/addReservationDates/{id}")
    public String proposeReservationDatesForReservation(@Valid @ModelAttribute("dateAndCost") ReservationDateAndTimeRequest date, BindingResult bindingResult, @PathVariable(name = "id") Integer reservationId, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("dateAndCost", date);
            model.addAttribute("reservationId", reservationId);
            model.addAttribute("errorMessage", "Invalid date or estimated cost!");
            return "create_date";
        }
        Date dateForReplace = new Date();
        String[] splittedDate = date.getDate().split("-");
        String year = splittedDate[0];
        String month = splittedDate[1];
        String day = splittedDate[2];
        String[] splittedTime = date.getTime().split(":");
        String hours = splittedTime[0];
        String minutes = splittedDate[1];
        dateForReplace.setYear(Integer.parseInt(year) - 1900);
        dateForReplace.setMonth(Integer.parseInt(month) - 1);
        dateForReplace.setDate(Integer.parseInt(day));
        dateForReplace.setHours(Integer.parseInt(hours));
        dateForReplace.setMinutes(Integer.parseInt(minutes));
        dateForReplace.setSeconds(0);

        ReservationDateRequestDto dto = new ReservationDateRequestDto();
        dto.setDate(dateForReplace);
        dto.setEstimatedCost(date.getEstimatedCost());

        mechanicService.proposeReservationDatesForReservation(reservationId, dto);
        ArrayList<ReservationResponseDto> reservationList =  mechanicService.getMechanicReservations();
        Collections.sort(reservationList, Comparator.comparing(ReservationResponseDto::getClientEmail));
        model.addAttribute("reservations", reservationList);
        return "redirect:/mechanic/reservations";
    }

//    @PatchMapping(path = "completeReservation/{id}")
//    public String completeReservation(@PathVariable(name = "id") Integer id){
//        return mechanicService.completeReservation(id);
//    }

    @GetMapping(path = "/deleteReservation/{id}")
    public String deleteReservation(@PathVariable(name = "id") Integer id){
        mechanicService.deleteReservation(id);
        return "redirect:/mechanic/reservations";
    }

    @GetMapping(path = "/completeReservation/{id}")
    public String completeReservation(@PathVariable(name = "id") Integer id, Model model){
        try{
            mechanicService.completeReservation(id);
        }
        catch (ReservationAlreadyCompletedException e){
            model.addAttribute("errorMessage", "Reservation already completed");
            ArrayList<ReservationResponseDto> reservationList =  mechanicService.getMechanicReservations();
            Collections.sort(reservationList, Comparator.comparing(ReservationResponseDto::getClientEmail));
            model.addAttribute("reservations", reservationList);
            return "reservations";
        }
        ArrayList<ReservationResponseDto> reservationList =  mechanicService.getMechanicReservations();
        Collections.sort(reservationList, Comparator.comparing(ReservationResponseDto::getClientEmail));
        model.addAttribute("reservations", reservationList);
        model.addAttribute("successMessage", "Reservation completed");
        //return "redirect:/mechanic/reservations";
        return "reservations";
    }

//    @GetMapping(path = "/reservations")
//    public ArrayList<ReservationResponseDto> getMechanicReservations(){
//        return mechanicService.getMechanicReservations();
//    }

    @GetMapping(path = "/reservations")
    public String getAllUsers(Model model) {
        ArrayList<ReservationResponseDto> reservationList =  mechanicService.getMechanicReservations();
        Collections.sort(reservationList, Comparator.comparing(ReservationResponseDto::getClientEmail));
        model.addAttribute("reservations", reservationList);
        return "reservations";
    }
}
