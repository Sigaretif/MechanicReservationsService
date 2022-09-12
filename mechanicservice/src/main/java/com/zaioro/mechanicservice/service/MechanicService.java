package com.zaioro.mechanicservice.service;

import com.zaioro.mechanicservice.communicates.ReservationCommunicates;
import com.zaioro.mechanicservice.dto.request.ReservationDateRequestDto;
import com.zaioro.mechanicservice.dto.response.ReservationResponseDto;
import com.zaioro.mechanicservice.exceptions.ReservationAlreadyCompletedException;
import com.zaioro.mechanicservice.exceptions.ReservationNotExistsException;
import com.zaioro.mechanicservice.exceptions.WrongReservationDtoException;
import com.zaioro.mechanicservice.model.ReservationDate;
import com.zaioro.mechanicservice.model.reservation.Reservation;
import com.zaioro.mechanicservice.model.reservation.ReservationStatus;
import com.zaioro.mechanicservice.repository.ReservationDateRepository;
import com.zaioro.mechanicservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MechanicService {
    private final ReservationRepository reservationRepository;
    private final ReservationDateRepository reservationDateRepository;

    public String proposeReservationDatesForReservation(Integer reservationId, ReservationDateRequestDto reservationDateRequestDto) throws ReservationNotExistsException, WrongReservationDtoException {
//        Optional<Reservation> foundReservation = reservationRepository.findById(reservationId);
//        if(foundReservation.isPresent()){
//            Reservation reservation = foundReservation.get();
//            for(ReservationDateRequestDto date: reservationDatesRequestDto){
//                ReservationDate reservationDate = ReservationDate.builder()
//                        .reservationDate(date.getDate())
//                        .reservation(reservation)
//                        .build();
//                reservationDateRepository.save(reservationDate);
//            }
//            reservation.setStatus(ReservationStatus.WAITING_FOR_DATE_CONFIRMATION);
//            reservationRepository.save(reservation);
//            return "Success";
//        }
//        throw new ReservationNotExistsException(ReservationCommunicates.RESERVATION_NOT_FOUND);
        Optional<Reservation> foundReservation = reservationRepository.findById(reservationId);
        if(foundReservation.isPresent()){
            Reservation reservation = foundReservation.get();
//            for(ReservationDateRequestDto date: reservationDatesRequestDto){
//                ReservationDate reservationDate = ReservationDate.builder()
//                        .reservationDate(date.getDate())
//                        .reservation(reservation)
//                        .build();
//                reservationDateRepository.save(reservationDate);
//            }
            if(reservationDateRequestDto.getDate() == null || reservationDateRequestDto.getEstimatedCost() == null || reservationDateRequestDto.getEstimatedCost().equals("")){
                throw new WrongReservationDtoException("Wrong date or cost");
            }
            else{
                reservation.setChoosedDate(reservationDateRequestDto.getDate());
                reservation.setEstimatedCost(reservationDateRequestDto.getEstimatedCost());
                reservation.setStatus(ReservationStatus.WAITING_FOR_DATE_CONFIRMATION);
                reservationRepository.save(reservation);
                return "Success";
            }
        }
        throw new ReservationNotExistsException(ReservationCommunicates.RESERVATION_NOT_FOUND);
    }

    @Transactional
    public String deleteReservation(Integer reservationId) throws ReservationNotExistsException {
        Optional<Reservation> foundReservation = reservationRepository.findById(reservationId);
        if(foundReservation.isPresent()){
            Reservation reservation = foundReservation.get();
            reservationRepository.deleteReservationUsingId(reservationId);
            return "Success";
        }
        throw new ReservationNotExistsException(ReservationCommunicates.RESERVATION_NOT_FOUND);
    }

    public String completeReservation(Integer reservationId) throws ReservationNotExistsException, ReservationAlreadyCompletedException {
        Optional<Reservation> foundReservation = reservationRepository.findById(reservationId);
        if(foundReservation.isPresent()){
            Reservation reservation = foundReservation.get();
            if(reservation.getStatus().equals(ReservationStatus.SERVICE_COMPLETED)){
                throw new ReservationAlreadyCompletedException(ReservationCommunicates.RESERVATION_ALREADY_COMPLETED);
            }
            reservation.setStatus(ReservationStatus.SERVICE_COMPLETED);
            reservationRepository.save(reservation);
            return "Success";
        }
        throw new ReservationNotExistsException(ReservationCommunicates.RESERVATION_NOT_FOUND);
    }

    public ArrayList<ReservationResponseDto> getMechanicReservations(){
        List<Reservation> allReservations = reservationRepository.findAll();
        ArrayList<ReservationResponseDto> reservations = new ArrayList<>();
        for(Reservation reservation: allReservations){
            reservations.add(ReservationResponseDto.builder()
                    .dates(reservation.getReservationDates().stream()
                            .map(ReservationDate::getReservationDate)
                            .collect(Collectors.toList()))
                    .estimatedCost(reservation.getEstimatedCost())
                    .carId(reservation.getCar().getId())
                    .carBrand(reservation.getCar().getBrand())
                    .carModel(reservation.getCar().getModel())
                    .clientId(reservation.getClient().getId())
                    .clientEmail(reservation.getClient().getEmail())
                    .serviceTypeId(reservation.getServiceType().getId())
                    .serviceType(reservation.getServiceType().getName())
                    .exactDescription(reservation.getExactDescription())
                    .status(reservation.getStatus())
                    .choosedDate(reservation.getChoosedDate())
                    .reservationID(reservation.getId())
                    .attachment(reservation.getAttachment())
                    .build());
        }
        return reservations;
    }
}
