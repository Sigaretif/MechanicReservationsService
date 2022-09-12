package com.zaioro.mechanicservice.dto.request;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserRegistrationDto {
    @NotEmpty(message = "Field cannot be blank")
    private String firstName;
    @NotEmpty(message = "Field cannot be blank")
    private String lastName;
    @NotEmpty(message = "Field cannot be blank")
    @Email(message = "Field must be a valid email")
    private String email;
    @NotEmpty(message = "Field cannot be blank")
    private String password;
    @NotEmpty(message = "Field cannot be blank")
    private String city;
    @NotEmpty(message = "Field cannot be blank")
    private String street;
    @NotEmpty(message = "Field cannot be blank")
    private String streetNumber;
    @NotEmpty(message = "Field cannot be blank")
    private String postalCode;
    @NotEmpty(message = "Field cannot be blank")
    private String postalRegion;
}
