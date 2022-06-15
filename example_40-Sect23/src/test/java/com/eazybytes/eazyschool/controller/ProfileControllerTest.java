package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void displayProfilePage() throws Exception {
        Map<String, Object> stringPersonMap = new HashMap<>();
        Person personMock = new Person();
        personMock.setPersonId(1);
        personMock.setName("Mock User");
        personMock.setMobileNumber("1111111111");

        stringPersonMap.put("loggedUser",personMock);

        mockMvc.perform(get("/displayProfile").sessionAttrs(stringPersonMap).with(user("Mock User")))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("errormsg"))
                .andExpect(view().name("profile.html"))
                .andExpect(content().string(containsString("My Profile")))
                .andExpect(content().string(not((containsString("oops...")))))
                .andDo(print());
    }
}