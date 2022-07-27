package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@RepositoryRestResource(path = "roles") just for documentation
@RepositoryRestResource(exported = false)
public interface RolesRepository extends JpaRepository<Roles, Integer> {

    List<Roles> getByRoleName(String roleName);
}
