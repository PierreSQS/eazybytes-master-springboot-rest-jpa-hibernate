package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.model.Roles;
import com.eazybytes.eazyschool.repository.RolesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {

    private final RolesRepository repo;

    public RolesService(RolesRepository repo) {
        this.repo = repo;
    }

    public List<Roles> findUserByRole(String role) {
        return repo.findByRoleName(role);
    }
}
