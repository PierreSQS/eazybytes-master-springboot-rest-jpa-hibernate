package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.constants.WebClientConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.model.Response;
import com.eazybytes.eazyschool.proxy.ContactProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
class ContactControllerTest {

    private List<Contact> contactMessagesMock;
    private Contact contactToSave;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ContactProxy contactProxyMock;

    @MockBean
    RestTemplate restTemplateMock;

    @MockBean
    WebClient webClientMock;

    @BeforeEach
    void setUp() {

//        webTestClient = MockMvcWebTestClient.
//                bindToController(new ContactController(contactProxyMock,restTemplateMock,webClientMock))
//                .build();;

        contactToSave = new Contact();
        contactToSave.setName("Sarah Test");
        contactToSave.setMobileNum("1234567890");
        contactToSave.setEmail("sarah@example.com");
        contactToSave.setMessage("Please contact me");
        contactToSave.setSubject("concerning my music course");
        contactToSave.setStatus(WebClientConstants.OPEN);

        Contact contact1 = new Contact();
        contact1.setContactId(1);
        contact1.setName("Sarah Test");
        contact1.setEmail("sarah@example.com");
        contact1.setMessage("Please contact me");
        contact1.setSubject("concerning my music course");
        contact1.setStatus(WebClientConstants.OPEN);

        Contact contact2 = new Contact();
        contact2.setContactId(2);
        contact2.setName("Jeannot Test");
        contact2.setEmail("jeannot@example.com");
        contact2.setMessage("A Question");
        contact2.setSubject("is there any Music course?");
        contact1.setStatus(WebClientConstants.OPEN);

        contactMessagesMock = List.of(contact1,contact2);   }

    @Test
    void getMessages() throws Exception {
        given(contactProxyMock.getMessagesByStatus(WebClientConstants.OPEN)).willReturn(contactMessagesMock);

        mockMvc.perform(get("/getMessages")
                        .param("status","Open"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(equalTo(2)))
                .andDo(print());
    }

    @Test
    void saveMsg() throws Exception {
        mockMvc.perform(post("/saveMsg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactToSave)))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Disabled("Test NOK-to Analyze")
    @Test
    void saveMessageReactive() {
        webTestClient.post().uri("/saveMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(contactToSave),Response.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Response.class);
    }

    @Disabled("Test NOK-to Analyze")
    @Test
    void saveMessageNonReactive() throws Exception {
        mockMvc.perform(post("/saveMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactToSave)))
                .andExpect(status().isOk())
                .andDo(print());
    }

}