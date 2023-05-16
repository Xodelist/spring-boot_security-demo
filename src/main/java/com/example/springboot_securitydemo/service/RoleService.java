package com.example.springboot_securitydemo.service;

import com.example.springboot_securitydemo.models.Role;
import com.example.springboot_securitydemo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class RoleService {

    private final RoleRepository repository;

    @Autowired
    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> getRoles() {
        return repository.findAll();

    }

    public Optional<Role> findByRolename(String rolename) {
        return repository.findByRolename(rolename);
    }
}
