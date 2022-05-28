package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<Object> add(@RequestBody User user) {;
        User newUser = this.userService.add(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(location).build();
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
