package com.example.demo.configuration.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaginatedResponse<T> {
    public int recordCount;
    public T response;
}
