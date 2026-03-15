package com.eazybytes.jobportal.company.controller;

import com.eazybytes.jobportal.support.AbstractControllerMvcTest;
import com.eazybytes.jobportal.support.JobPortalMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

@JobPortalMvcTest(LoggingController.class)
class LoggingControllerMvcTest extends AbstractControllerMvcTest {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @Test
    void testLoggingReturnsOk() {
        restTestClient.get()
                .uri("/api/logging/public")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Logging tested successfully");
    }
}




