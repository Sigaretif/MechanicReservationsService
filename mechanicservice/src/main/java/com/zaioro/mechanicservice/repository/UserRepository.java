package com.zaioro.mechanicservice.repository;

import com.zaioro.mechanicservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
}
