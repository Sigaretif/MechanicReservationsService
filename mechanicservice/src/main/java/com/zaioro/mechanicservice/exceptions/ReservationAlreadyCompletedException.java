package com.zaioro.mechanicservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReservationAlreadyCompletedException extends RuntimeException{
    public ReservationAlreadyCompletedException(String msg){
        super(msg);
    }
}
