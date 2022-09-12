package com.zaioro.mechanicservice.repository;

import com.zaioro.mechanicservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.email = :username")
    User findByUsername(@Param("username") String username);

    List<User> findByFirstNameAndLastName(String firstName, String lastName);
}
