package com.example.demo.configuration.response;

import java.util.List;

public class PaginatedResponse<T extends List> {
    public int recordCount;
    public T response;

    public PaginatedResponse(T input) {
        this.recordCount = input.size();
        this.response = input;
    }
}
