package com.zaioro.mechanicservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class WrongRoleException extends RuntimeException{
    public WrongRoleException(String msg){
        super(msg);
    }
}
