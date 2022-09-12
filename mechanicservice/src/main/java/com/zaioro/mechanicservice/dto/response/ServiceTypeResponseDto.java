package com.zaioro.mechanicservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServiceTypeResponseDto {
    private Integer id;
    private String serviceType;
}
