package com.example.demo.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public void add(Role role) {
        this.roleRepository.save(role);
    }
}
