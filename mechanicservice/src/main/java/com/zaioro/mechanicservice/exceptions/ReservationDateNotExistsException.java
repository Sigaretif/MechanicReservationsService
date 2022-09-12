package com.zaioro.mechanicservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReservationDateNotExistsException extends RuntimeException{
    public ReservationDateNotExistsException(String msg){
        super(msg);
    }
}
