package com.zaioro.mechanicservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserBlockedException extends RuntimeException{
    public UserBlockedException(String msg){
        super(msg);
    }
}
