package com.zaioro.mechanicservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String city;
    private String street;
    private String streetNumber;
    private String postalCode;
    private String postalRegion;
}
