package com.zaioro.mechanicservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReservationOperationAlreadyDoneException extends RuntimeException{
    public ReservationOperationAlreadyDoneException(String msg){
        super(msg);
    }
}
