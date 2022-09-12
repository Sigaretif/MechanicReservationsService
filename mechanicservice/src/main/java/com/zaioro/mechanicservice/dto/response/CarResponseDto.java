package com.zaioro.mechanicservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CarResponseDto {
    private Integer id;
    private String brand;
    private String model;
    private Integer yearOfProduction;
    private String body;
}
