package com.example.library_rest_api.auth.repo;

import com.example.library_rest_api.auth.models.Role;
import com.example.library_rest_api.auth.models.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByRole(Role role);
}
