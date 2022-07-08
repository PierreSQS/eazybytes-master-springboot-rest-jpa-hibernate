package com.eazybytes.eazyschool.rest;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.repository.ContactRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(ContactRestController.class)
class ContactRestControllerTest {

    private Contact validContact, contactWithInvalidEmail;
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
        validContact = new Contact();
        validContact.setName("Pierrot Test");
        validContact.setEmail("pierrot@example.com");
        validContact.setMobileNum("1234567890");
        validContact.setMessage("Please contact me");
        validContact.setStatus(EazySchoolConstants.CLOSED);
        validContact.setSubject("Very urgent");

        contactWithInvalidEmail = new Contact();
        contactWithInvalidEmail.setName("Pierrot Test");
        contactWithInvalidEmail.setEmail("pierrot§example.com");


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
        contactMessagesMock = List.of(validContact);
        given(contactRepoSrvMock.findByStatus(anyString())).willReturn(contactMessagesMock);

        mockMvc.perform(get("/api/contact/getMessagesByStatusWithBody")
                        .content(objectMapper.writeValueAsString(validContact))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(equalTo(1)))
                .andExpect(jsonPath("$[0].email").value(equalTo("pierrot@example.com")))
                .andExpect(jsonPath("$[0].status").value(equalTo(EazySchoolConstants.CLOSED)))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "Mock User")
    void saveMsgValidData() throws Exception {
        // Given
        mockMvc.perform(post("/api/contact/saveMsg")
                        .content(objectMapper.writeValueAsString(validContact))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("invocationForm","Mock Unit Test"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("isMsgSaved","true"))
                .andExpect(jsonPath("$.statusCode").value(equalTo("201 CREATED")))
                .andExpect(jsonPath("$.statusMsg").value(equalTo("Message saved successfully!")))
                .andDo(print());

        verify(contactRepoSrvMock).save(any());

    }

    @Disabled("Wrong assertions because of the actual GlobalExceptionController!!!!")
    @Test
    @WithMockUser(username = "Mock User")
    void saveMsgInvalidData() throws Exception {
        // Given
        mockMvc.perform(post("/api/contact/saveMsg")
                        .content(objectMapper.writeValueAsString(contactWithInvalidEmail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("invocationForm","Mock Unit Test"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("isMsgSaved","true"))
                .andExpect(jsonPath("$.statusCode").value(equalTo("201 CREATED")))
                .andExpect(jsonPath("$.statusMsg").value(equalTo("Message saved successfully!")))
                .andDo(print());

        verify(contactRepoSrvMock).save(any());

    }
}