package com.zaioro.mechanicservice.service;

import com.zaioro.mechanicservice.communicates.UserCommunicates;
import com.zaioro.mechanicservice.dto.request.UserLoginRequestDto;
import com.zaioro.mechanicservice.dto.request.UserRegistrationDto;
import com.zaioro.mechanicservice.dto.response.UserLoginResponseDto;
import com.zaioro.mechanicservice.exceptions.AccountAwaitingException;
import com.zaioro.mechanicservice.exceptions.UserAlreadyExistsException;
import com.zaioro.mechanicservice.exceptions.UserBlockedException;
import com.zaioro.mechanicservice.exceptions.UserNotExistsException;
import com.zaioro.mechanicservice.exceptions.WrongPasswordException;
import com.zaioro.mechanicservice.model.Address;
import com.zaioro.mechanicservice.model.user.User;
import com.zaioro.mechanicservice.model.user.UserRole;
import com.zaioro.mechanicservice.model.user.UserStatus;
import com.zaioro.mechanicservice.repository.AddressRepository;
import com.zaioro.mechanicservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public String registerUser(UserRegistrationDto userRegistrationDto) throws AccountAwaitingException, UserAlreadyExistsException {
        Optional<User> foundUser = userRepository.findByEmail(userRegistrationDto.getEmail());
        if(foundUser.isPresent()){
            if(foundUser.get().getStatus().equals(UserStatus.UNVERIFIED)){
                throw new AccountAwaitingException(UserCommunicates.WAITING_FOR_APPROVAL);
            }
            else{
                throw new UserAlreadyExistsException(UserCommunicates.USER_ALREADY_EXISTS);
            }
        }

        Address address;
        Optional<Address> foundAddress = addressRepository.findAddressByCityAndStreetAndStreetNumberAndPostalCodeAndPostalRegion(userRegistrationDto.getCity(), userRegistrationDto.getStreet(), userRegistrationDto.getStreetNumber(), userRegistrationDto.getPostalCode(), userRegistrationDto.getPostalRegion());
        if(foundAddress.isPresent()){
            address = foundAddress.get();
        }
        else{
            address = Address.builder()
                    .city(userRegistrationDto.getCity())
                    .street(userRegistrationDto.getStreet())
                    .streetNumber(userRegistrationDto.getStreetNumber())
                    .postalCode(userRegistrationDto.getPostalCode())
                    .postalRegion(userRegistrationDto.getPostalRegion())
                    .build();
            addressRepository.save(address);
        }

        User user = User.builder()
                .firstName(userRegistrationDto.getFirstName())
                .lastName(userRegistrationDto.getLastName())
                .email(userRegistrationDto.getEmail())
                .password(BCrypt.hashpw(userRegistrationDto.getPassword(), BCrypt.gensalt(12)))
                .role(UserRole.UNDETERMINED)
                .status(UserStatus.UNVERIFIED)
                .address(address)
                .build();
        userRepository.save(user);
        return UserCommunicates.USER_SUCCESSFULLY_REGISTERED;
    }

    public UserLoginResponseDto loginUser(UserLoginRequestDto userLoginRequestDto) throws UserBlockedException, WrongPasswordException, UserNotExistsException, AccountAwaitingException {
        Optional<User> foundUser = userRepository.findByEmail(userLoginRequestDto.getEmail());
        if(foundUser.isPresent()){
            User user = foundUser.get();
            if(user.getStatus().equals(UserStatus.BLOCKED)){
                throw new UserBlockedException(UserCommunicates.USER_BLOCKED);
            }
            if(user.getStatus().equals(UserStatus.UNVERIFIED)){
                throw new AccountAwaitingException(UserCommunicates.WAITING_FOR_APPROVAL);
            }
            if(BCrypt.checkpw(userLoginRequestDto.getPassword(), user.getPassword())){
                return UserLoginResponseDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .role(user.getRole())
                        .status(user.getStatus())
                        .build();
            }
            throw new WrongPasswordException(UserCommunicates.WRONG_PASSWORD);
        }
        throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
    }
}
