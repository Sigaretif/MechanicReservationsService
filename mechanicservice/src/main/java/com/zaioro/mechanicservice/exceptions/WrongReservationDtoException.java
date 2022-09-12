package com.zaioro.mechanicservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class WrongReservationDtoException extends RuntimeException{
    public WrongReservationDtoException(String msg){
        super(msg);
    }
}
