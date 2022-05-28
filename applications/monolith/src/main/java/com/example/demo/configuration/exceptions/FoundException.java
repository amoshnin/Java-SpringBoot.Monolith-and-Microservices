package com.example.demo.configuration.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class FoundException extends RuntimeException {
    public FoundException(String message) {
        super(message);
    }
}