package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicController.class)
class PublicControllerTest {

    public static final String ERR_MSGS1 = "Confirm Password must not be blank";
    public static final String ERR_MSGS2 = "Name must not be blank";
    public static final String ERR_MSGS3 = "Mobile number must not be blank";
    public static final String ERR_MSGS4 = "Password must not be blank";
    public static final String ERR_MSGS5 = "Please choose a strong password";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PersonService personSrvMock;

    Person person;


    @BeforeEach
    void setUp() {
        person = new Person();
        person.setName("Pierrot");
        person.setEmail("pierrot@gmail.com");
        person.setConfirmEmail("pierrot@gmail.com");
        person.setMobileNumber("1234567890");
        person.setPwd("galerien");
        person.setConfirmPwd("galerien");
    }

    @Test
    void displayRegistrationPage() throws Exception {
        mockMvc.perform(get("/public/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("person"))
                .andExpect(view().name("register.html"))
                .andExpect(content().string(containsString("<h3 class=\"title-style\">Registration Form</h3>")))
                .andDo(print());
    }

    @Test
    void createUserOK() throws Exception {

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name",person.getName());
        multiValueMap.add("mobileNumber",person.getMobileNumber());
        multiValueMap.add("email",person.getEmail());
        multiValueMap.add("confirmEmail",person.getConfirmEmail());
        multiValueMap.add("pwd",person.getPwd());
        multiValueMap.add("confirmPwd",person.getConfirmPwd());

        mockMvc.perform(post("/public/createUser")
                        .params(multiValueMap)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                // Doesn't work with a redirect!!!!
//                .andExpect(model().attributeHasNoErrors("person"))
                .andExpect(content().string(not(containsString(ERR_MSGS1))))
                .andExpect(content().string(not(containsString(ERR_MSGS2))))
                .andExpect(content().string(not(containsString(ERR_MSGS3))))
                .andExpect(content().string(not(containsString(ERR_MSGS4))))
                .andExpect(content().string(not(containsString(ERR_MSGS5))))
                .andDo(print());

        verify(personSrvMock).saveUser(any());
    }

    @Test
    void createUserWithoutPWD() throws Exception {

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name",person.getName());
        multiValueMap.add("mobileNumber",person.getMobileNumber());
        multiValueMap.add("email",person.getEmail());
        multiValueMap.add("confirmEmail",person.getConfirmEmail());


        mockMvc.perform(post("/public/createUser")
                        .params(multiValueMap)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                // Works in this case since we return a view!!!!
                .andExpect(model().attributeHasFieldErrors("person","pwd"))
                .andExpect(content().string(containsString(ERR_MSGS1)))
                .andExpect(content().string(containsString(ERR_MSGS4)))
                 .andDo(print());
    }
}