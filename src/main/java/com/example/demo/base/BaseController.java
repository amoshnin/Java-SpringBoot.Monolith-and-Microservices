package com.example.demo.base;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/")
public class BaseController {
    @GetMapping
    public String index() {
        return "Hello World!";
    }
}
