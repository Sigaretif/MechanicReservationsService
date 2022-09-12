package com.zaioro.mechanicservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class ReservationDateResponseDto {
    private Integer reservationId;
    private Integer reservationDateId;
    @JsonFormat(pattern="dd/MM/yyyy h:mm a")
    private Date date;

}
