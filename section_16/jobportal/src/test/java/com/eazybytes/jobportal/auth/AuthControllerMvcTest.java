package com.eazybytes.jobportal.auth;

import com.eazybytes.jobportal.aspects.RegisterValidationAspect;
import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.dto.LoginRequestDto;
import com.eazybytes.jobportal.dto.RegisterRequestDto;
import com.eazybytes.jobportal.entity.JobPortalUser;
import com.eazybytes.jobportal.entity.Role;
import com.eazybytes.jobportal.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.repository.RoleRepository;
import com.eazybytes.jobportal.security.util.JwtUtil;
import com.eazybytes.jobportal.support.AbstractControllerMvcTest;
import com.eazybytes.jobportal.support.JobPortalMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@JobPortalMvcTest(AuthController.class)
@ImportAutoConfiguration(AopAutoConfiguration.class)
@Import(RegisterValidationAspect.class)
class AuthControllerMvcTest extends AbstractControllerMvcTest {

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    @SuppressWarnings("unused")
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JobPortalUserRepository jobPortalUserRepository;

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private CompromisedPasswordChecker compromisedPasswordChecker;

    @Test
    void apiLoginWithValidCredentialsReturnsTokenAndUser() throws Exception {
        JobPortalUser jobPortalUser = createUser("Jane Doe", "jane@jobportal.test", ApplicationConstants.ROLE_JOB_SEEKER);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                jobPortalUser,
                null,
                AuthorityUtils.createAuthorityList(ApplicationConstants.ROLE_JOB_SEEKER));

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtUtil.generateJwtToken(authentication)).thenReturn("signed-jwt-token");

        LoginRequestDto loginRequestDto = new LoginRequestDto("jane@jobportal.test", "SecurePass123");

        withCsrf(restTestClient.post()
                .uri("/api/auth/login/public")
                .contentType(JSON))
                .body(toJson(loginRequestDto))
                .exchange()
                .expectStatus().isOk();

        withCsrf(restTestClient.post()
                .uri("/api/auth/login/public")
                .contentType(JSON))
                .body(toJson(loginRequestDto))
                .exchange()
                .expectBody()
                .jsonPath("$.message").isEqualTo(HttpStatus.OK.getReasonPhrase())
                .jsonPath("$.jwtToken").isEqualTo("signed-jwt-token")
                .jsonPath("$.user.userId").isEqualTo(7)
                .jsonPath("$.user.email").isEqualTo("jane@jobportal.test")
                .jsonPath("$.user.role").isEqualTo(ApplicationConstants.ROLE_JOB_SEEKER);
    }

    @Test
    void apiLoginWithoutCsrfReturnsForbidden() throws Exception {
        restTestClient.post()
                .uri("/api/auth/login/public")
                .contentType(JSON)
                .body(toJson(new LoginRequestDto("jane@jobportal.test", "SecurePass123")))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void apiLoginWithBadCredentialsReturnsUnauthorized() throws Exception {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        withCsrf(restTestClient.post()
                .uri("/api/auth/login/public")
                .contentType(JSON))
                .body(toJson(new LoginRequestDto("jane@jobportal.test", "wrong-password")))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid username or password")
                .jsonPath("$.user").doesNotExist()
                .jsonPath("$.jwtToken").doesNotExist();
    }

    @Test
    void registerUserWithValidRequestReturnsCreated() throws Exception {
        Role role = new Role();
        role.setId(11L);
        role.setName(ApplicationConstants.ROLE_JOB_SEEKER);

        RegisterRequestDto registerRequestDto = new RegisterRequestDto(
                "Sarah Smith",
                "sarah@jobportal.test",
                "1234567890",
                "SecurePass123");

        when(compromisedPasswordChecker.check(anyString())).thenReturn(new CompromisedPasswordDecision(false));
        when(jobPortalUserRepository.readUserByEmailOrMobileNumber(anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequestDto.password())).thenReturn("encoded-password");
        when(roleRepository.findRoleByName(ApplicationConstants.ROLE_JOB_SEEKER)).thenReturn(Optional.of(role));

        withCsrf(restTestClient.post()
                .uri("/api/auth/register/public")
                .contentType(JSON))
                .body(toJson(registerRequestDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class).isEqualTo("User registered successfully");

        verify(jobPortalUserRepository).save(any(JobPortalUser.class));
    }

    @Test
    void registerUserWithDuplicateEmailReturnsBadRequest() throws Exception {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto(
                "Sarah Smith",
                "sarah@jobportal.test",
                "1234567890",
                "SecurePass123");

        JobPortalUser existingUser = createUser("Existing User", "sarah@jobportal.test", ApplicationConstants.ROLE_JOB_SEEKER);
        existingUser.setMobileNumber("1234567890");

        when(compromisedPasswordChecker.check(anyString())).thenReturn(new CompromisedPasswordDecision(false));
        when(jobPortalUserRepository.readUserByEmailOrMobileNumber(registerRequestDto.email(), registerRequestDto.mobileNumber()))
                .thenReturn(Optional.of(existingUser));

        withCsrf(restTestClient.post()
                .uri("/api/auth/register/public")
                .contentType(JSON))
                .body(toJson(registerRequestDto))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.email").isEqualTo("Email is already registered")
                .jsonPath("$.mobileNumber").isEqualTo("Mobile number is already registered");
    }

    private JobPortalUser createUser(String name, String email, String roleName) {
        Role role = new Role();
        role.setId(3L);
        role.setName(roleName);

        JobPortalUser user = new JobPortalUser();
        user.setId(7L);
        user.setName(name);
        user.setEmail(email);
        user.setMobileNumber("1234567890");
        user.setRole(role);
        return user;
    }
}




