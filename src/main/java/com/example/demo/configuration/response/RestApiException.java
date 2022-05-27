package com.example.demo.configuration.response;

public class RestApiException extends RuntimeException {
    public RestApiException(String message) {
        super(message);
    }
}
