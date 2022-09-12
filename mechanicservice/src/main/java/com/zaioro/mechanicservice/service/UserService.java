package com.zaioro.mechanicservice.service;

import com.zaioro.mechanicservice.communicates.CarCommunicates;
import com.zaioro.mechanicservice.communicates.ReservationCommunicates;
import com.zaioro.mechanicservice.communicates.UserCommunicates;
import com.zaioro.mechanicservice.dto.response.CarResponseDto;
import com.zaioro.mechanicservice.dto.response.ReservationDateResponseDto;
import com.zaioro.mechanicservice.dto.response.ReservationResponseDto;
import com.zaioro.mechanicservice.dto.response.ServiceTypeResponseDto;
import com.zaioro.mechanicservice.dto.response.UserResponseDto;
import com.zaioro.mechanicservice.exceptions.AccountAwaitingException;
import com.zaioro.mechanicservice.exceptions.CarNotExistsException;
import com.zaioro.mechanicservice.exceptions.ReservationNotExistsException;
import com.zaioro.mechanicservice.exceptions.UserAlreadyBlockedException;
import com.zaioro.mechanicservice.exceptions.UserAlreadyVerifiedException;
import com.zaioro.mechanicservice.exceptions.UserNotExistsException;
import com.zaioro.mechanicservice.exceptions.WrongRoleException;
import com.zaioro.mechanicservice.model.ReservationDate;
import com.zaioro.mechanicservice.model.car.Car;
import com.zaioro.mechanicservice.model.reservation.Reservation;
import com.zaioro.mechanicservice.model.serviceType.ServiceType;
import com.zaioro.mechanicservice.model.user.User;
import com.zaioro.mechanicservice.model.user.UserRole;
import com.zaioro.mechanicservice.model.user.UserStatus;
import com.zaioro.mechanicservice.repository.CarRepository;
import com.zaioro.mechanicservice.repository.ReservationDateRepository;
import com.zaioro.mechanicservice.repository.ReservationRepository;
import com.zaioro.mechanicservice.repository.ServiceTypeRepository;
import com.zaioro.mechanicservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final CarRepository carRepository;

    public String getUserStatus(Integer userId) throws UserNotExistsException{
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            User foundUser = user.get();
            return  foundUser.getStatus();
        }
        else{
            throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
        }
    }

    public ArrayList<UserResponseDto> getUserByName(String firstName, String lastName) throws UserNotExistsException {
        ArrayList<UserResponseDto> usersToReturn = new ArrayList<>();
        List<User> foundUsers = userRepository.findByFirstNameAndLastName(firstName, lastName);
        return castUserToUserResponseDto(usersToReturn, foundUsers);
    }

    public UserResponseDto getUserById(Integer userId) throws UserNotExistsException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User foundUser = user.get();
            UserResponseDto userResponseDto = UserResponseDto.builder()
                    .id(foundUser.getId())
                    .firstName(foundUser.getFirstName())
                    .lastName(foundUser.getLastName())
                    .status(foundUser.getStatus())
                    .role(foundUser.getRole())
                    .email(foundUser.getEmail())
                    .city(foundUser.getAddress().getCity())
                    .street(foundUser.getAddress().getStreet())
                    .streetNumber(foundUser.getAddress().getStreetNumber())
                    .build();
            return userResponseDto;

        } else {
            throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
        }
    }

    public ArrayList<UserResponseDto> getAllUsers() throws UserNotExistsException {
        ArrayList<UserResponseDto> usersToReturn = new ArrayList<>();
        List<User> foundUsers = userRepository.findAll();
        return castUserToUserResponseDto(usersToReturn, foundUsers);

    }

    private ArrayList<UserResponseDto> castUserToUserResponseDto(ArrayList<UserResponseDto> usersToReturn, List<User> foundUsers) throws UserNotExistsException {
        for (User user : foundUsers) {
            if(!user.getRole().equals(UserRole.MECHANIC)){
                usersToReturn.add(UserResponseDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .status(user.getStatus())
                        .role(user.getRole())
                        .email(user.getEmail())
                        .city(user.getAddress().getCity())
                        .street(user.getAddress().getStreet())
                        .streetNumber(user.getAddress().getStreetNumber())
                        .build());
            }
        }
        if (usersToReturn.size() != 0)
            return usersToReturn;
        else
            throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
    }


    public String deleteUserById(Integer id) throws UserNotExistsException {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            userRepository.delete(user);
            return UserCommunicates.USER_SUCCESSFULLY_DELETED;
        }
        throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
    }

    public String deleteCarById(Integer id) throws CarNotExistsException {
        Optional<Car> foundCar = carRepository.findById(id);
        if (foundCar.isPresent()) {
            Car car = foundCar.get();
            String userId = car.getUser().getId().toString();
            carRepository.delete(car);
            return userId;
        }
        throw new CarNotExistsException(CarCommunicates.CAR_NOT_EXISTS);
    }

    public String verifyUserById(Integer id) throws UserNotExistsException, UserAlreadyVerifiedException {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            if (user.getStatus().equals(UserStatus.VERIFIED)) {
                throw new UserAlreadyVerifiedException(UserCommunicates.USER_ALREADY_VERIFIED);
            } else {
                user.setStatus(UserStatus.VERIFIED);
                user = userRepository.save(user);
                return UserCommunicates.USER_SUCCESSFULLY_VERIFIED;
            }
        } else {
            throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
        }
    }

    public User verifyUser(User user) throws UserNotExistsException, UserAlreadyVerifiedException {
        if (user.getStatus().equals(UserStatus.VERIFIED)) {
            throw new UserAlreadyVerifiedException(UserCommunicates.USER_ALREADY_VERIFIED);
        } else {
            user.setStatus(UserStatus.VERIFIED);
            return userRepository.save(user);
        }
    }

    public String blockUserById(Integer id) throws UserNotExistsException, UserAlreadyBlockedException {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            if (user.getStatus().equals(UserStatus.BLOCKED)) {
                throw new UserAlreadyBlockedException(UserCommunicates.USER_ALREADY_BLOCKED);
            } else {
                user.setStatus(UserStatus.BLOCKED);
                userRepository.save(user);
                return UserCommunicates.USER_SUCCESSFULLY_BLOCKED;
            }
        } else {
            throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
        }
    }

    public String changeRoleById(Integer id, String role) throws UserNotExistsException, WrongRoleException, AccountAwaitingException {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            if (!user.getStatus().equals(UserStatus.VERIFIED)) {
                throw new AccountAwaitingException(UserCommunicates.USER_NOT_VERIFIED);
            }
            switch (role) {
                case UserRole.UNDETERMINED:
                case UserRole.CLIENT:
                case UserRole.MECHANIC:
                case UserRole.ADMIN: {
                    user.setRole(role);
                    userRepository.save(user);
                    break;
                }
                default: {
                    throw new WrongRoleException(UserCommunicates.WRONG_ROLE);
                }
            }
            return UserCommunicates.ROLE_SUCCESSFULLY_CHANGED;
        } else {
            throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
        }
    }

    public ArrayList<CarResponseDto> getUsersCars(Integer id) throws UserNotExistsException, CarNotExistsException {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            Set<Car> usersCars = user.getCars();
//            if (usersCars.isEmpty()) {
//                throw new CarNotExistsException(CarCommunicates.CAR_NOT_EXISTS);
//            }
            ArrayList<CarResponseDto> carsToReturn = new ArrayList<>();
            for (Car car : usersCars) {
                carsToReturn.add(CarResponseDto.builder()
                        .id(car.getId())
                        .model(car.getModel())
                        .brand(car.getBrand())
                        .yearOfProduction(car.getYearOfProduction())
                        .body(car.getBody())
                        .build());
            }
            return carsToReturn;
        }
        throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
    }

    public int getAllCarsAmount(){
        List<Car> cars = carRepository.findAll();
        return cars.size();
    }

    public ArrayList<ReservationResponseDto> getUserReservations(Integer userId) throws UserNotExistsException{
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
            User foundUser = user.get();
            List<Reservation> allReservations = reservationRepository.findAllByClient(foundUser);
            ArrayList<ReservationResponseDto> reservations = new ArrayList<>();
            for(Reservation reservation: allReservations){
                reservations.add(ReservationResponseDto.builder()
//                        .dates(reservation.getReservationDates().stream()
//                                .map(ReservationDate::getReservationDate)
//                                .collect(Collectors.toList()))
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
        } else{
            throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
        }
    }

    public ArrayList<ReservationDateResponseDto> getUserReservationDates(Integer reservationId) throws ReservationNotExistsException {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if(reservation.isPresent()){
            Reservation foundReservation = reservation.get();
            Set<ReservationDate> reservationDates = foundReservation.getReservationDates();
            List<ReservationDate> castedReservationDates = new ArrayList<>(reservationDates);
            ArrayList<ReservationDateResponseDto> reservationDateResponseDtos = new ArrayList<>();
            for(ReservationDate reservationDate: castedReservationDates){
                reservationDateResponseDtos.add(ReservationDateResponseDto.builder()
                        .reservationId(reservationId)
                        .reservationDateId(reservationDate.getId())
                        .date(reservationDate.getReservationDate())
                        .build());
            }
            return reservationDateResponseDtos;
        } else{
            throw new ReservationNotExistsException(ReservationCommunicates.RESERVATION_NOT_FOUND);
        }
    }

    public ArrayList<ServiceTypeResponseDto> getAllServiceTypes(){
        List<ServiceType> allServiceTypes = serviceTypeRepository.findAll();
        ArrayList<ServiceTypeResponseDto> serviceTypeResponseDtos = new ArrayList<>();
        for(ServiceType serviceType: allServiceTypes){
            serviceTypeResponseDtos.add(ServiceTypeResponseDto.builder()
                    .id(serviceType.getId())
                    .serviceType(serviceType.getName())
                    .build());
        }
        return serviceTypeResponseDtos;
    }
}
