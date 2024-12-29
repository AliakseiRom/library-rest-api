package com.example.library_rest_api.model;

import com.example.library_rest_api.auth.models.Role;
import com.example.library_rest_api.auth.models.RoleEntity;
import com.example.library_rest_api.auth.repo.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RoleInitializer {

    @Autowired
    RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {
        Arrays.stream(Role.values()).forEach(role -> {
            if (roleRepository.findByRole(role).isEmpty()) {
                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setRole(role);
                roleRepository.save(roleEntity);
            }
        });
    }
}
