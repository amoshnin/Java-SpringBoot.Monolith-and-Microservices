package com.example.demo.limit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/limits")
public class LimitController {
    @GetMapping(path="item")
    public Limit getItem() {
        return new Limit(1, 1000);
    }
}
