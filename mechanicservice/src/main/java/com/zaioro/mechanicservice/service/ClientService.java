package com.zaioro.mechanicservice.service;

import com.zaioro.mechanicservice.communicates.CarCommunicates;
import com.zaioro.mechanicservice.communicates.ReservationCommunicates;
import com.zaioro.mechanicservice.communicates.ServiceTypesCommunicates;
import com.zaioro.mechanicservice.communicates.UserCommunicates;
import com.zaioro.mechanicservice.dto.request.CarAddRequestDto;
import com.zaioro.mechanicservice.dto.request.ReservationRequestDto;
import com.zaioro.mechanicservice.dto.response.CarResponseDto;
import com.zaioro.mechanicservice.dto.response.ServiceTypeResponseDto;
import com.zaioro.mechanicservice.exceptions.CarNotExistsException;
import com.zaioro.mechanicservice.exceptions.ReservationDateNotExistsException;
import com.zaioro.mechanicservice.exceptions.ReservationNotExistsException;
import com.zaioro.mechanicservice.exceptions.ReservationOperationAlreadyDoneException;
import com.zaioro.mechanicservice.exceptions.ServiceTypeNotExistsException;
import com.zaioro.mechanicservice.exceptions.UserNotExistsException;
import com.zaioro.mechanicservice.model.ReservationDate;
import com.zaioro.mechanicservice.model.car.Car;
import com.zaioro.mechanicservice.model.reservation.Reservation;
import com.zaioro.mechanicservice.model.reservation.ReservationStatus;
import com.zaioro.mechanicservice.model.serviceType.ServiceType;
import com.zaioro.mechanicservice.model.user.User;
import com.zaioro.mechanicservice.repository.CarRepository;
import com.zaioro.mechanicservice.repository.ReservationDateRepository;
import com.zaioro.mechanicservice.repository.ReservationRepository;
import com.zaioro.mechanicservice.repository.ServiceTypeRepository;
import com.zaioro.mechanicservice.repository.UserRepository;
import com.zaioro.mechanicservice.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationDateRepository reservationDateRepository;

    public String addCar(CarAddRequestDto carAddRequestDto, Integer clientId) throws UserNotExistsException {
        Optional<User> foundUser = userRepository.findById(clientId);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            Car car = Car.builder()
                    .brand(carAddRequestDto.getBrand())
                    .model(carAddRequestDto.getModel())
                    .yearOfProduction(carAddRequestDto.getYearOfProduction())
                    .body(carAddRequestDto.getBody())
                    .user(user)
                    .build();
            carRepository.save(car);
            return "Success";
        }
        throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
    }

    public String addAttachment(Integer userId, Integer reservationId, MultipartFile multipartFile) throws IOException {
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            if (!fileName.equals("")) {
                String uploadDir = "user-attachments/user" + userId + "/reservation" + reservationId + "/";
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
                return "Success";
            }
        }
        return "Failure";
    }

    public String createReservation(ReservationRequestDto reservationRequestDto, Integer clientId, MultipartFile multipartFile) throws UserNotExistsException, CarNotExistsException, ServiceTypeNotExistsException, IOException {
        Optional<User> foundUser = userRepository.findById(clientId);
        if (foundUser.isPresent()) {
            User client = foundUser.get();
            Optional<Car> foundCar = carRepository.findById(reservationRequestDto.getCarId());
            if (foundCar.isPresent()) {
                Car car = foundCar.get();
                Optional<ServiceType> foundServiceType = serviceTypeRepository.findById(reservationRequestDto.getServiceTypeId());
                if (foundServiceType.isPresent()) {
                    ServiceType serviceType = foundServiceType.get();
                    Reservation reservation = Reservation.builder()
                            .client(client)
                            .car(car)
                            .serviceType(serviceType)
                            .exactDescription(reservationRequestDto.getExactDescription())
                            .status(ReservationStatus.WAITING_FOR_DATES)
                            .build();
                    reservation = reservationRepository.save(reservation);
                    if (multipartFile != null) {
                        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                        if(!fileName.equals("")){
                            String uploadDir = "attachments/user" + clientId + "/reservation" + reservation.getId() + "/";
                            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
                            reservation.setAttachment(fileName);
                            reservationRepository.save(reservation);
                        }
                    }
                    return "Success";
                }
                throw new ServiceTypeNotExistsException(ServiceTypesCommunicates.SERVICE_TYPE_NOT_EXISTS);
            }
            throw new CarNotExistsException(CarCommunicates.CAR_NOT_EXISTS);
        }
        throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
    }

    public String confirmReservation(Integer reservationId) throws ReservationNotExistsException, ReservationDateNotExistsException, ReservationOperationAlreadyDoneException {
//        Optional<Reservation> foundReservation = reservationRepository.findById(reservationId);
//        if (foundReservation.isPresent()) {
//            Reservation reservation = foundReservation.get();
//            Optional<ReservationDate> foundReservationDate = reservationDateRepository.findById(dateId);
//            if (foundReservationDate.isPresent()) {
//                ReservationDate reservationDate = foundReservationDate.get();
//                reservation.setChoosedDate(reservationDate.getReservationDate());
//                reservation.setStatus(ReservationStatus.RESERVATION_CONFIRMED);
//                reservationRepository.save(reservation);
//                return "Success";
//            }
//            throw new ReservationDateNotExistsException(ReservationCommunicates.RESERVATION_DATE_NOT_FOUND);
//        }
//        throw new ReservationNotExistsException(ReservationCommunicates.RESERVATION_NOT_FOUND);
        Optional<Reservation> foundReservation = reservationRepository.findById(reservationId);
        if (foundReservation.isPresent()) {
            Reservation reservation = foundReservation.get();
//            Optional<ReservationDate> foundReservationDate = reservationDateRepository.findById(dateId);
//            if (foundReservationDate.isPresent()) {
//                ReservationDate reservationDate = foundReservationDate.get();
//                reservation.setChoosedDate(reservationDate.getReservationDate());
//                reservation.setStatus(ReservationStatus.RESERVATION_CONFIRMED);
//                reservationRepository.save(reservation);
//                return "Success";
//            }
//            throw new ReservationDateNotExistsException(ReservationCommunicates.RESERVATION_DATE_NOT_FOUND);

            if(reservation.getStatus().equals(ReservationStatus.RESERVATION_CONFIRMED)){
                throw new ReservationOperationAlreadyDoneException(ReservationCommunicates.OPERATION_ALREADY_DONE);
            }
            reservation.setStatus(ReservationStatus.RESERVATION_CONFIRMED);
            reservationRepository.save(reservation);

            return reservation.getClient().getId().toString();
        }
        throw new ReservationNotExistsException(ReservationCommunicates.RESERVATION_NOT_FOUND);
    }

    public String rejectReservation(Integer reservationId) throws ReservationNotExistsException, ReservationOperationAlreadyDoneException {
        Optional<Reservation> foundReservation = reservationRepository.findById(reservationId);
        if (foundReservation.isPresent()) {
            Reservation reservation = foundReservation.get();
            if(reservation.getStatus().equals(ReservationStatus.DATES_REJECTED)){
                throw new ReservationOperationAlreadyDoneException(ReservationCommunicates.OPERATION_ALREADY_DONE);
            }
            reservation.setStatus(ReservationStatus.DATES_REJECTED);
            reservation.setChoosedDate(null);
            reservationRepository.save(reservation);
            return "Success";
        }
        throw new ReservationNotExistsException(ReservationCommunicates.RESERVATION_NOT_FOUND);
    }

    public String cancelReservation(Integer reservationId) throws ReservationNotExistsException, ReservationOperationAlreadyDoneException {
        Optional<Reservation> foundReservation = reservationRepository.findById(reservationId);
        if (foundReservation.isPresent()) {
            Reservation reservation = foundReservation.get();
            if(reservation.getStatus().equals(ReservationStatus.RESERVATION_CANCELED)){
                throw new ReservationOperationAlreadyDoneException(ReservationCommunicates.OPERATION_ALREADY_DONE);
            }
            reservation.setStatus(ReservationStatus.RESERVATION_CANCELED);
            reservation.setChoosedDate(null);
            reservationRepository.save(reservation);
            return "Success";
        }
        throw new ReservationNotExistsException(ReservationCommunicates.RESERVATION_NOT_FOUND);
    }
}
