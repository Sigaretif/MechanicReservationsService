package com.zaioro.mechanicservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReservationNotExistsException extends RuntimeException{
    public ReservationNotExistsException(String msg){
        super(msg);
    }
}
