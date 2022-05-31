package com.zaioro.mechanicservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyBlockedException extends RuntimeException{
    public UserAlreadyBlockedException(String msg){
        super(msg);
    }
}
