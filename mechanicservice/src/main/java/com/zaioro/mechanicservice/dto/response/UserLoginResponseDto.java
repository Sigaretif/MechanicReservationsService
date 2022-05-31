package com.zaioro.mechanicservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserLoginResponseDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String role;
    private String status;
}
