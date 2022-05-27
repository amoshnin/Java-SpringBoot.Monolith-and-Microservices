package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(path="list")
    public List<User> getList() {
        return this.userService.getList();
    }

    @GetMapping(path="item/{userId}")
    public User getItem(@PathVariable Long userId) {
        return this.userService.getItem(userId);
    }

    @PostMapping(path="item")
    public void add(@RequestBody User user) {;
        this.userService.add(user);
    }

    @DeleteMapping(path="item/{userId}")
    public void delete(@PathVariable Long userId) {
        this.userService.delete(userId);
    }

    @PutMapping(path="item")
    public void update(@RequestBody User user) {
        this.userService.update(user);
    }
}
