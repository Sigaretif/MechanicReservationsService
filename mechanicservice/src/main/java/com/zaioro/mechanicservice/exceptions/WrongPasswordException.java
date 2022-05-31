package com.zaioro.mechanicservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException(String msg){
        super(msg);
    }
}
