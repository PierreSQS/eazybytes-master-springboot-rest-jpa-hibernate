package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
class ContactControllerTest {

    private MultiValueMap<String, String> multiValueMap;
    private Contact contact;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ContactService contactSrvMock;

    @BeforeEach
    void setUp() {
        // Given
        contact = new Contact();
        contact.setName("Pierrot Test");
        contact.setEmail("pierrot@example.com");
        contact.setMessage("Please contact me");
        contact.setSubject("Very urgent");

        multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name",contact.getName());
        multiValueMap.add("email", contact.getEmail());
        multiValueMap.add("message", contact.getMessage());
        multiValueMap.add("subject",contact.getSubject());
    }

    @Test
    void displayContactPage() throws Exception {
        // When, Then
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact.html"))
                .andExpect(content().string(containsString("<h3 class=\"title-style\">Contact Us</h3>")))
                .andDo(print());

    }

    @Test
    void saveMessageOK() throws Exception {
        // Given
        contact.setMobileNum("2121212122");
        multiValueMap.add("mobileNum",contact.getMobileNum());

        // When, Then
        mockMvc.perform(post("/saveMsg").params(multiValueMap))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/contact"))
                .andDo(print());

        verify(contactSrvMock).saveMessageDetails(contact);

    }

    @Test
    void saveMessageMobileNrWrong() throws Exception {
        // Given
        contact.setMobileNum("21212121221");
        multiValueMap.add("mobileNum",contact.getMobileNum());

        // When, Then
        mockMvc.perform(post("/saveMsg").params(multiValueMap))
                .andExpect(status().isOk())
                .andExpect(view().name("contact.html"))
                .andExpect(model().attributeHasErrors())
                .andExpect(model().attributeHasFieldErrors("contact","mobileNum"))
                .andExpect(content().string(containsString("Mobile number must be 10 digits")))
                .andDo(print());

    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void displayContactMessages() throws Exception {
        mockMvc.perform(get("/displayMessages").param("status", EazySchoolConstants.OPEN))
                .andExpect(status().isOk())
                .andExpect(view().name("messages.html"))
                .andExpect(content().string(containsString("Open Contact Messages")))
                .andDo(print());
    }
}