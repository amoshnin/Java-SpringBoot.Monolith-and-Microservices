package com.example.demo.role;

import com.example.demo.configuration.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role add(Role role) {
        return this.roleRepository.save(role);
    }

    public Role getItem(Long roleId) {
        Optional<Role> row = this.roleRepository.findById(roleId);
        if (!row.isPresent()) {
            throw new NotFoundException(String.format("Role with ID: %s doesn't exist", roleId));
        }
        return row.get();
    }
}
