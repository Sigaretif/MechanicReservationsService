package com.zaioro.mechanicservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@Setter
public class CarAddRequestDto {
    @NotEmpty(message = "Field cannot be blank")
    private String brand;
    @NotEmpty(message = "Field cannot be blank")
    private String model;
    @NotNull(message = "Field cannot be blank")
    @Min(value = 1900, message = "Field must be a valid year")
    @Max(value = 2023, message = "Field must be a valid year")
    private Integer yearOfProduction;
    @NotEmpty(message = "Field cannot be blank")
    private String body;
//    private Integer userId;
}
