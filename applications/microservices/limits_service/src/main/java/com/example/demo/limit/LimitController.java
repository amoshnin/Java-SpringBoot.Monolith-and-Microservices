package com.example.demo.limit;

import com.example.demo.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/limits")
public class LimitController {
    @Autowired
    private Configuration configuration;

    @GetMapping(path="item")
    public Limit getItem() {
        return new Limit(this.configuration.getMinimum(), this.configuration.getMaximum());
    }
}
