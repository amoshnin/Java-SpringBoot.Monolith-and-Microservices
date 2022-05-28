package com.example.demo.configuration.pagination;

import lombok.Getter;

import java.util.Optional;

@Getter
public class PaginationObject {
    private int pageNumber;
    private int pageSize;

    public PaginationObject(Optional<Integer> offset, Optional<Integer> pageSize) {
        this.pageNumber = offset.orElse(0);
        this.pageSize = pageSize.orElse(10);
      }
}
