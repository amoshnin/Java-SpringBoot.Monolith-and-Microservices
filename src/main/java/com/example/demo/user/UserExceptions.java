package com.example.demo.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

@ResponseStatus(HttpStatus.FOUND)
class UserFoundException extends RuntimeException {
    public UserFoundException(String message) {
        super(message);
    }
}