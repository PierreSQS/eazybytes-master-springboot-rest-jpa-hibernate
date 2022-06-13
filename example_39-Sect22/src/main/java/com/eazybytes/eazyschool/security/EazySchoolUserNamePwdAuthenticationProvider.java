package com.eazybytes.eazyschool.security;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Roles;
import com.eazybytes.eazyschool.repository.PersonRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EazySchoolUserNamePwdAuthenticationProvider implements AuthenticationProvider {
    private final PersonRepository personRepo;

    public EazySchoolUserNamePwdAuthenticationProvider(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        Person person = personRepo.findByEmail(username);

        if (person != null && username.equals(person.getEmail()) && pwd.equals(person.getPwd())) {
            List<Roles> authRolesList = new ArrayList<>();
            authRolesList.add(person.getRoles());
            return new UsernamePasswordAuthenticationToken(username,pwd,getGrantedAuthorities(authRolesList));
        } else {
            throw new BadCredentialsException("Invalid Credentials!!!");
        }

    }

    private List<? extends GrantedAuthority> getGrantedAuthorities(List<Roles> rolesList) {
        return rolesList.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.toString()))
                .toList();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
