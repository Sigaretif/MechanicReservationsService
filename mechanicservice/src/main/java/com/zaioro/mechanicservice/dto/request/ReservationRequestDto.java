package com.zaioro.mechanicservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ReservationRequestDto {
    //private Integer clientId;
    private Integer carId;
    private Integer serviceTypeId;
    @NotEmpty(message = "Field cannot be blank")
    private String exactDescription;
}
