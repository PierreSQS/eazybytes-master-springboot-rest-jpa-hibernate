package com.eazybytes.jobportal.security;

import com.eazybytes.jobportal.entity.JobPortalUser;
import com.eazybytes.jobportal.repository.JobPortalUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JobPortalUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = Objects.requireNonNull(authentication.getCredentials()).toString();

        JobPortalUser jobPortalUser = jobPortalUserRepository.findJobPortalUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User details not found for the user: " + email)
        );

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(jobPortalUser.getRole().getName()));

        if (passwordEncoder.matches(pwd, jobPortalUser.getPasswordHash())) {
            return new UsernamePasswordAuthenticationToken(jobPortalUser, null, authorities);
        } else {
            throw new BadCredentialsException("Invalid password!");
        }
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
