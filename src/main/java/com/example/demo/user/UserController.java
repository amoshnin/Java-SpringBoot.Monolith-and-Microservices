package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path="item")
    public void add(@RequestBody User user) {;
        this.userService.add(user);
    }
}
