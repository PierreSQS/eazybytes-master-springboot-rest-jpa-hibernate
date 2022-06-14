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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EazySchoolUserNamePwdAuthenticationProvider implements AuthenticationProvider {
    private final PersonRepository personRepo;

    private final PasswordEncoder passwordEncoder;

    public EazySchoolUserNamePwdAuthenticationProvider(PersonRepository personRepo, PasswordEncoder passwordEncoder) {
        this.personRepo = personRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        Person person = personRepo.findByEmail(username);

        if (person != null && username.equals(person.getEmail()) && passwordEncoder.matches(pwd,person.getPwd())) {
            List<Roles> authRolesList = List.of(person.getRoles());
            return new UsernamePasswordAuthenticationToken(username,null,getGrantedAuthorities(authRolesList));
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
