package com.eazybytes.jobportal.contact.controller;

import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.dto.ContactResponseDto;
import com.eazybytes.jobportal.support.AbstractControllerMvcTest;
import com.eazybytes.jobportal.support.JobPortalMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@JobPortalMvcTest(ContactController.class)
class ContactControllerMvcTest extends AbstractControllerMvcTest {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private IContactService contactService;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @Test
    void saveContactMsgWithCsrfReturnsCreated() throws Exception {
        ContactRequestDto requestDto = new ContactRequestDto(
                "alice@jobportal.test",
                "I am interested in your employer account.",
                "Alice Doe",
                "Need information",
                "Employer");

        when(contactService.saveContact(any(ContactRequestDto.class))).thenReturn(true);

        withCsrf(restTestClient.post()
                .uri("/api/contacts/public")
                .contentType(JSON))
                .body(toJson(requestDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class).isEqualTo("Request processed successfully");
    }

    @Test
    void saveContactMsgWithoutCsrfReturnsForbidden() throws Exception {
        ContactRequestDto requestDto = new ContactRequestDto(
                "alice@jobportal.test",
                "I am interested in your employer account.",
                "Alice Doe",
                "Need information",
                "Employer");

        restTestClient.post()
                .uri("/api/contacts/public")
                .contentType(JSON)
                .body(toJson(requestDto))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void saveContactMsgWithInvalidPayloadReturnsBadRequest() throws Exception {
        ContactRequestDto requestDto = new ContactRequestDto(
                "invalid-email",
                "hey",
                "Bob",
                "Hi",
                "Unknown");

        withCsrf(restTestClient.post()
                .uri("/api/contacts/public")
                .contentType(JSON))
                .body(toJson(requestDto))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.email").isEqualTo("Invalid email address")
                .jsonPath("$.message").isEqualTo("Message must be between 5 and 500 characters")
                .jsonPath("$.name").isEqualTo("Name must be between 5 and 30 characters")
                .jsonPath("$.subject").isEqualTo("Subject must be between 5 and 150 characters")
                .jsonPath("$.userType").isEqualTo("UserType must be one of: Job Seeker, Employer, Other");
    }

    @Test
    void fetchNewContactMsgsWithoutAuthenticationReturnsUnauthorized() {
        restTestClient.get()
                .uri("/api/contacts/admin")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void fetchNewContactMsgsWithNonAdminRoleReturnsForbidden() {
        restTestClient.get()
                .uri("/api/contacts/admin")
                .header("Authorization", bearerToken(userToken()))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void fetchNewContactMsgsWithAdminRoleReturnsMessages() {
        ContactResponseDto dto = createResponseDto(1L, "NEW");
        when(contactService.fetchNewContactMsgs()).thenReturn(List.of(dto));

        restTestClient.get()
                .uri("/api/contacts/admin")
                .header("Authorization", bearerToken(adminToken()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].status").isEqualTo("NEW");
    }

    @Test
    void fetchNewContactMsgsWithSortPassesArgumentsAndReturnsMessages() {
        ContactResponseDto dto = createResponseDto(2L, "NEW");
        when(contactService.fetchNewContactMsgsWithSort("name", "asc")).thenReturn(List.of(dto));

        restTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contacts/sort/admin")
                        .queryParam("sortBy", "name")
                        .queryParam("sortDir", "asc")
                        .build())
                .header("Authorization", bearerToken(adminToken()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Contact 2");

        verify(contactService).fetchNewContactMsgsWithSort("name", "asc");
    }

    @Test
    void fetchNewContactMsgsWithPaginationAndSortReturnsPage() {
        ContactResponseDto dto = createResponseDto(3L, "NEW");
        when(contactService.fetchContactMsgsWithPaginationAndSort("NEW", 0, 5, "createdAt", "desc"))
                .thenReturn(new PageImpl<>(List.of(dto), PageRequest.of(0, 5), 1));

        restTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contacts/page/admin")
                        .queryParam("pageNumber", 0)
                        .queryParam("pageSize", 5)
                        .queryParam("sortBy", "createdAt")
                        .queryParam("sortDir", "desc")
                        .build())
                .header("Authorization", bearerToken(adminToken()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content[0].id").isEqualTo(3)
                .jsonPath("$.content[0].status").isEqualTo("NEW")
                .jsonPath("$.page.totalElements").isEqualTo(1);
    }

    @Test
    void closeContactMsgWithAdminRoleAndCsrfReturnsOk() {
        when(contactService.closeContactMsg(15L)).thenReturn(true);

        withCsrf(restTestClient.patch()
                .uri("/api/contacts/{id}/status/admin", 15L)
                .header("Authorization", bearerToken(adminToken()))
                .contentType(JSON))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Contact Message successfully closed");
    }

    @Test
    void closeContactMsgWithMissingIdReturnsBadRequest() {
        when(contactService.closeContactMsg(99L)).thenReturn(false);

        withCsrf(restTestClient.patch()
                .uri("/api/contacts/{id}/status/admin", 99L)
                .header("Authorization", bearerToken(adminToken()))
                .contentType(JSON))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("the contact path is wrong since the id doesn't exist");
    }

    private ContactResponseDto createResponseDto(Long id, String status) {
        return new ContactResponseDto(
                id,
                "Contact " + id,
                "contact" + id + "@jobportal.test",
                "Employer",
                "Subject " + id,
                "Message content " + id,
                status,
                Instant.parse("2026-03-15T08:30:00Z"));
    }
}




