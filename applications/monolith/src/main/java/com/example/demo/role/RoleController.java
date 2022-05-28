package com.example.demo.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path="api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping(path="item/{roleId}")
    public Role getItem(@PathVariable Long roleId) {
        return this.roleService.getItem(roleId);
    }

    @PostMapping(path="item")
    public ResponseEntity<Object> add(@RequestBody Role role) {
        Role newRole = this.roleService.add(role);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{roleId}")
                .buildAndExpand(newRole.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}
