package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Roles;
import com.eazybytes.eazyschool.repository.PersonRepository;
import com.eazybytes.eazyschool.repository.RolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PersonService {
    private final PersonRepository personRepo;

    private final RolesRepository rolesRepo;

    public PersonService(PersonRepository personRepo, RolesRepository rolesRepo) {
        this.personRepo = personRepo;
        this.rolesRepo = rolesRepo;
    }

    public Person saveUser(Person person) {
        List<Roles> roles = rolesRepo.findByRoleName(EazySchoolConstants.STUDENT_ROLE);
        log.info("####### the found Roles: {}",roles);
        person.setRoles(roles.get(0));
        return personRepo.save(person);
    }
}
