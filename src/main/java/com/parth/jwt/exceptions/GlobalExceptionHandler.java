package com.parth.jwt.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public String  customBadCredentialsException(BadCredentialsException ex){
        return ex.getMessage();
    }
}
