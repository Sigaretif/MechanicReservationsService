package com.zaioro.mechanicservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AccountAwaitingException extends RuntimeException{
    public AccountAwaitingException(String msg){
        super(msg);
    }
}
