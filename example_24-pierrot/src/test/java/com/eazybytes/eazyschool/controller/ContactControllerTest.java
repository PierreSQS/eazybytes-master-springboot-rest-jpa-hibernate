package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ContactService contactSrvMock;

    @BeforeEach
    void setUp() {
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
    void saveMessage() throws Exception {
        // Given
        Contact contact = new Contact();
        contact.setName("Pierrot Test");
        contact.setEmail("pierrot@example.com");
        contact.setMessage("Please contact me");
        contact.setSubject("Very urgent");
        contact.setMobileNum("21212121221");

        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name",contact.getName());
        multiValueMap.add("email", contact.getEmail());
        multiValueMap.add("message", contact.getMessage());
        multiValueMap.add("mobileNum",contact.getMobileNum());
        multiValueMap.add("subject",contact.getSubject());

        // When, Then
        mockMvc.perform(post("/saveMsg").params(multiValueMap))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/contact"))
                .andDo(print());

        verify(contactSrvMock).saveMessageDetails(contact);

    }
}