package com.example.demo.configuration.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SortObject {
    private String sortField;
    private Boolean descendingSort;
}