package com.eazybytes.jobportal.security;

import com.eazybytes.jobportal.support.AbstractControllerMvcTest;
import com.eazybytes.jobportal.support.JobPortalMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

@JobPortalMvcTest(CsrfController.class)
class CsrfControllerMvcTest extends AbstractControllerMvcTest {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @Test
    void csrfTokenReturnsCookieAndTokenPayload() {
        restTestClient.get()
                .uri("/api/csrf-token/public")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueMatches("Set-Cookie", ".*XSRF-TOKEN=.*")
                .expectBody()
                .jsonPath("$.headerName").isEqualTo("X-XSRF-TOKEN")
                .jsonPath("$.parameterName").isEqualTo("_csrf")
                .jsonPath("$.token").isNotEmpty();
    }
}




