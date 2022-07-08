package com.eazybytes.eazyschool.rest;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.repository.ContactRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(ContactRestController.class)
class ContactRestControllerTest {

    private Contact contact1;
    private List<Contact> contactMessagesMock;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ContactRepository contactRepoSrvMock;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Given
        contact1 = new Contact();
        contact1.setName("Pierrot Test");
        contact1.setEmail("pierrot@example.com");
        contact1.setMessage("Please contact me");
        contact1.setStatus(EazySchoolConstants.CLOSED);
        contact1.setSubject("Very urgent");

        Contact contact2 = new Contact();
        contact2.setContactId(1);
        contact2.setName("Sarah Test");
        contact2.setEmail("sarah@example.com");
        contact2.setMessage("Please contact me");
        contact2.setSubject("concerning my music course");
        contact2.setStatus(EazySchoolConstants.OPEN);

        Contact contact3 = new Contact();
        contact3.setContactId(2);
        contact3.setName("Jeannot Test");
        contact3.setEmail("jeannot@example.com");
        contact3.setMessage("A Question");
        contact3.setSubject("is there any Music course?");
        contact2.setStatus(EazySchoolConstants.OPEN);

        contactMessagesMock = List.of(contact2,contact3);

    }

    @Test
    @WithMockUser(username = "Mock User")
    void getContactMessagesByStatus() throws Exception {
        // Given
        given(contactRepoSrvMock.findByStatus(anyString())).willReturn(contactMessagesMock);

        mockMvc.perform(get("/api/contact/getMessagesByStatus?status={}", EazySchoolConstants.OPEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(equalTo(2)))
                .andExpect(jsonPath("$[0].email").value(equalTo("sarah@example.com")))
                .andExpect(jsonPath("$[1].email").value(equalTo("jeannot@example.com")))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "Mock User")
    void getContactMessagesByStatusWithBody() throws Exception {
        // Given
        contactMessagesMock = List.of(contact1);
        given(contactRepoSrvMock.findByStatus(anyString())).willReturn(contactMessagesMock);

        mockMvc.perform(get("/api/contact/getMessagesByStatusWithBody")
                        .content(objectMapper.writeValueAsString(contact1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(equalTo(1)))
                .andExpect(jsonPath("$[0].email").value(equalTo("pierrot@example.com")))
                .andExpect(jsonPath("$[0].status").value(equalTo(EazySchoolConstants.CLOSED)))
                .andDo(print());
    }
}