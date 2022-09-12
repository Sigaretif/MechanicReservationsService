package com.zaioro.mechanicservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReservationDateAndTimeRequest {
    @NotEmpty(message = "Field cannot be blank")
    private String estimatedCost;
    @NotEmpty(message = "Field cannot be blank")
    private String date;
    @NotEmpty(message = "Field cannot be blank")
    private String time;
}
