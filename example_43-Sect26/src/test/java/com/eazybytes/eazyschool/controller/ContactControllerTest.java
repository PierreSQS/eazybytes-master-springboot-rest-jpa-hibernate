package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
class ContactControllerTest {

    private MultiValueMap<String, String> multiValueMap;
    private Contact contact1;
    private List<Contact> contactMessages;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ContactService contactSrvMock;

    @BeforeEach
    void setUp() {
        // Given
        contact1 = new Contact();
        contact1.setName("Pierrot Test");
        contact1.setEmail("pierrot@example.com");
        contact1.setMessage("Please contact me");
        contact1.setSubject("Very urgent");

        Contact contact2 = new Contact();
        contact2.setContactId(1);
        contact2.setName("Sarah Test");
        contact2.setEmail("sarah@example.com");
        contact2.setMessage("Please contact me");
        contact2.setSubject("concerning my music course");

        Contact contact3 = new Contact();
        contact3.setContactId(2);
        contact3.setName("Jeannot Test");
        contact3.setEmail("jeannot@example.com");
        contact3.setMessage("A Question");
        contact3.setSubject("is there any Music course?");

        contactMessages = List.of(contact2,contact3);

        multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name", contact1.getName());
        multiValueMap.add("email", contact1.getEmail());
        multiValueMap.add("message", contact1.getMessage());
        multiValueMap.add("subject", contact1.getSubject());
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
        contact1.setMobileNum("2121212122");
        multiValueMap.add("mobileNum", contact1.getMobileNum());

        // When, Then
        mockMvc.perform(post("/saveMsg").params(multiValueMap))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/contact"))
                .andDo(print());

        verify(contactSrvMock).saveMessageDetails(any());

    }

    @Test
    void saveMessageMobileNrWrong() throws Exception {
        // Given
        contact1.setMobileNum("21212121221");
        multiValueMap.add("mobileNum", contact1.getMobileNum());

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
    void displayContactMessagesSortByNameAsc() throws Exception {
        // Given
        int pageNum = 1;
        int totalElmts = 10;
        int pageSize = 2;
        String sortField = "name";
        String sortDir= "asc";

        Pageable pageable = PageRequest.of(pageNum, pageSize ,Sort.by(sortField));

        Page<Contact> page = new PageImpl<>(contactMessages,pageable,totalElmts);

        given(contactSrvMock.findMsgsWithOpenStatus(pageNum,sortField,sortDir)).willReturn(page);

        multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("sortField",sortField);
        multiValueMap.add("sortDir",sortDir);

        mockMvc.perform(get("/displayMessages/page/{pageNum}",pageNum)
                        .params(multiValueMap))
                .andExpect(status().isOk())
                .andExpect(model().attribute("contactMsgs",
                        hasItem(hasProperty("email",equalTo("sarah@example.com")))))
                .andExpect(model().attribute("currentPage",equalTo(pageNum)))
                .andExpect(model().attribute("totalPages",equalTo(totalElmts/pageSize)))
                .andExpect(model().attribute("currentPage",equalTo(pageNum)))
                .andExpect(model().attribute("sortField",equalTo(sortField)))
                .andExpect(model().attribute("sortDir",equalTo(sortDir)))
                .andExpect(model().attribute("reverseSortDir",sortDir.equals("asc")?"desc":"asc"))
                .andExpect(view().name("messages.html"))
                .andExpect(content().string(containsString("Open Contact Messages")))
                .andExpect(content().string(containsString("<td>Sarah Test</td>")))
                .andExpect(content().string(containsString("<td>Jeannot Test</td>")))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "Mock User")
    void closeMessageWithAuthenticatedUser() throws Exception {
        // Given
        given(contactSrvMock.updateContactStatus(anyInt())).willReturn(true);
        mockMvc.perform(get("/closeMsg").param("id","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/displayMessages/page/1?sortField=name&sortDir=desc"))
                .andDo(print());

    }

}