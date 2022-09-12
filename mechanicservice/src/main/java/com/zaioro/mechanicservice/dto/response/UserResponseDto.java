package com.zaioro.mechanicservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String status;
    private String city;
    private String street;
    private String streetNumber;
}
