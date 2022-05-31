package com.zaioro.mechanicservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarAddRequestDto {
    private String brand;
    private String model;
    private Integer yearOfProduction;
    private String body;
    private Integer userId;
}
