package com.eazybytes.jobportal.company.controller;

import com.eazybytes.jobportal.company.service.ICompanyService;
import com.eazybytes.jobportal.dto.CompanyDto;
import com.eazybytes.jobportal.support.AbstractControllerMvcTest;
import com.eazybytes.jobportal.support.JobPortalMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;

@JobPortalMvcTest(CompanyController.class)
class CompanyControllerMvcTest extends AbstractControllerMvcTest {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private ICompanyService companyService;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @Test
    void getAllCompaniesReturnsPublicCompanyList() {
        CompanyDto company = new CompanyDto(
                1L,
                "Acme Corp",
                "logo.png",
                "Tech",
                "100-500",
                new BigDecimal("4.80"),
                "Berlin",
                2015,
                "A growing tech company",
                120,
                "https://acme.example",
                Instant.parse("2026-03-15T08:30:00Z"),
                List.of());

        when(companyService.getAllCompanies()).thenReturn(List.of(company));

        restTestClient.get()
                .uri("/api/companies/public")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo("Acme Corp")
                .jsonPath("$[0].industry").isEqualTo("Tech");
    }
}




