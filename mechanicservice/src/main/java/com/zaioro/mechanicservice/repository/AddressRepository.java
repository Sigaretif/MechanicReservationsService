package com.zaioro.mechanicservice.repository;

import com.zaioro.mechanicservice.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findAddressByCityAndStreetAndStreetNumberAndPostalCodeAndPostalRegion(String city, String street, String streetNumber,String postalCode, String postalRegion);
}
