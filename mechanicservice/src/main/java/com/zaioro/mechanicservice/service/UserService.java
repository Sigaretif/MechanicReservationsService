package com.zaioro.mechanicservice.service;

import com.zaioro.mechanicservice.communicates.UserCommunicates;
import com.zaioro.mechanicservice.dto.response.UserResponseDto;
import com.zaioro.mechanicservice.exceptions.AccountAwaitingException;
import com.zaioro.mechanicservice.exceptions.UserAlreadyBlockedException;
import com.zaioro.mechanicservice.exceptions.UserAlreadyVerifiedException;
import com.zaioro.mechanicservice.exceptions.UserNotExistsException;
import com.zaioro.mechanicservice.exceptions.WrongRoleException;
import com.zaioro.mechanicservice.model.user.User;
import com.zaioro.mechanicservice.model.user.UserRole;
import com.zaioro.mechanicservice.model.user.UserStatus;
import com.zaioro.mechanicservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ArrayList<UserResponseDto> getUserByName(String firstName, String lastName) throws UserNotExistsException{
        ArrayList<UserResponseDto> usersToReturn = new ArrayList<>();
        List<User> foundUsers = userRepository.findByFirstNameAndLastName(firstName, lastName);
        return castUserToUserResponseDto(usersToReturn, foundUsers);
    }

    public ArrayList<UserResponseDto> getAllUsers() throws UserNotExistsException{
        ArrayList<UserResponseDto> usersToReturn = new ArrayList<>();
        List<User> foundUsers = userRepository.findAll();
        return castUserToUserResponseDto(usersToReturn, foundUsers);

    }

    private ArrayList<UserResponseDto> castUserToUserResponseDto(ArrayList<UserResponseDto> usersToReturn, List<User> foundUsers) throws UserNotExistsException{
        for(User user: foundUsers){
            usersToReturn.add(UserResponseDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .status(user.getStatus())
                    .role(user.getRole())
                    .email(user.getEmail())
                    .build());
        }
        if(usersToReturn.size() != 0)
            return usersToReturn;
        else
            throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
    }


    public String deleteUserById(Integer id) throws UserNotExistsException{
        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()){
            User user = foundUser.get();
            userRepository.delete(user);
            return UserCommunicates.USER_SUCCESSFULLY_DELETED;
        }
        throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
    }

    public String verifyUserById(Integer id) throws UserNotExistsException, UserAlreadyVerifiedException {
        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()){
            User user = foundUser.get();
            if(user.getStatus().equals(UserStatus.VERIFIED)){
                throw new UserAlreadyVerifiedException(UserCommunicates.USER_ALREADY_VERIFIED);
            }
            else{
                user.setStatus(UserStatus.VERIFIED);
                userRepository.save(user);
                return UserCommunicates.USER_SUCCESSFULLY_VERIFIED;
            }
        }
        else{
            throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
        }
    }

    public String blockUserById(Integer id) throws UserNotExistsException, UserAlreadyBlockedException {
        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()){
            User user = foundUser.get();
            if(user.getStatus().equals(UserStatus.BLOCKED)){
                throw new UserAlreadyBlockedException(UserCommunicates.USER_ALREADY_BLOCKED);
            }
            else{
                user.setStatus(UserStatus.BLOCKED);
                userRepository.save(user);
                return UserCommunicates.USER_SUCCESSFULLY_BLOCKED;
            }
        }
        else{
            throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
        }
    }

    public String changeRoleById(Integer id, String role) throws UserNotExistsException, WrongRoleException, AccountAwaitingException {
        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()){
            User user = foundUser.get();
            if(!user.getStatus().equals(UserStatus.VERIFIED)){
                throw new AccountAwaitingException(UserCommunicates.USER_NOT_VERIFIED);
            }
            switch(role){
                case UserRole.UNDETERMINED:
                case UserRole.CLIENT:
                case UserRole.MECHANIC:
                case UserRole.ADMIN: {
                    user.setRole(role);
                    userRepository.save(user);
                    break;
                }
                default:{
                    throw new WrongRoleException(UserCommunicates.WRONG_ROLE);
                }
            }
            return UserCommunicates.ROLE_SUCCESSFULLY_CHANGED;
        }
        else{
            throw new UserNotExistsException(UserCommunicates.USER_NOT_EXISTS);
        }
    }
}
