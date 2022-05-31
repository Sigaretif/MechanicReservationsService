package com.zaioro.mechanicservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyVerifiedException extends RuntimeException{
    public UserAlreadyVerifiedException(String msg){
        super(msg);
    }
}
