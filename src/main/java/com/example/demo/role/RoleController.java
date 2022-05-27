package com.example.demo.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping(path="item")
    public void add(@RequestBody Role role) {
        this.roleService.add(role);
    }
}
