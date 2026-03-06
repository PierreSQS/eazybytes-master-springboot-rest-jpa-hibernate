package com.eazybytes.jobportal.security;

import com.eazybytes.jobportal.entity.JobPortalUser;
import com.eazybytes.jobportal.repository.JobPortalUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JobPortalUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final JobPortalUserRepository jobPortalUserRepo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        JobPortalUser jobPortalUser = jobPortalUserRepo.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User details not found for the user: " + email)
                );

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(jobPortalUser.getRole().getName()));

        if (passwordEncoder.matches(password, jobPortalUser.getPasswordHash())) {
            return new UsernamePasswordAuthenticationToken(jobPortalUser, null, authorities);
        } else {
            throw new BadCredentialsException("Invalid password!");
        }
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
