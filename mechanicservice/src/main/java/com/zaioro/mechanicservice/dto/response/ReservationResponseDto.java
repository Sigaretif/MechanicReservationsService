package com.zaioro.mechanicservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
public class ReservationResponseDto {
    private Integer reservationID;
    @JsonFormat(pattern="dd/MM/yyyy h:mm a")
    private List<Date> dates;
    private Integer clientId;
    private String clientEmail;
    private Integer carId;
    private String carBrand;
    private String carModel;
    private Integer serviceTypeId;
    private String serviceType;
    private String exactDescription;
    private String status;
    private Date choosedDate;
    private String attachment;
    private String estimatedCost;
}
