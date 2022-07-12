package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.EazyClass;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PersonRepository personRepoMock;

    Person personMock;

    @BeforeEach
    void setUp() {
        personMock = new Person();
        personMock.setPersonId(1);
        personMock.setMobileNumber("1111111111");
    }

    @Test
    void displayDashboardForMockUserROLE_USER_NoAssignedClass() throws Exception {
        // Given
        personMock.setName("Mock User");

        given(personRepoMock.findByEmail(anyString())).willReturn(personMock);

        // When and Then
        mockMvc.perform(get("/dashboard").with(user("Mock User")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("username",equalTo("Mock User")))
                .andExpect(model().attribute("roles",equalTo("[ROLE_USER]")))
                .andExpect(view().name("dashboard.html"))
                .andExpect(content().string(containsString("Welcome - Mock User")))
                .andExpect(content().string(containsString("Profile")))
                .andExpect(content().string(containsString("Courses")))
                .andExpect(content().string(not(containsString("Messages"))))
                .andExpect(content().string(not(containsString("Your assigned class is"))))
                .andDo(print());
    }

    @Test
    void displayDashboardForMockUserROLE_USER_WithAssignedClass() throws Exception {
        // Given
        EazyClass enrolledClass = new EazyClass();
        enrolledClass.setClassId(1);
        enrolledClass.setName("Class1");

        personMock.setName("Mock User");
        personMock.setEazyClass(enrolledClass);

        given(personRepoMock.findByEmail(anyString())).willReturn(personMock);

        // When and Then
        mockMvc.perform(get("/dashboard").with(user("Mock User")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("username",equalTo("Mock User")))
                .andExpect(model().attribute("roles",equalTo("[ROLE_USER]")))
                .andExpect(model().attribute("enrolledClass",equalTo("Class1")))
                .andExpect(view().name("dashboard.html"))
                .andExpect(content().string(containsString("Welcome - Mock User")))
                .andExpect(content().string(containsString("Profile")))
                .andExpect(content().string(containsString("Courses")))
                .andExpect(content().string(not(containsString("Messages"))))
                .andExpect(content().string((containsString("Your assigned class is - [" + enrolledClass.getName() +"]"))))
                .andDo(print());
    }

    @Test
    void displayDashboardForMockUserROLE_ADMIN() throws Exception {
        // Given
        personMock.setName("Mock Admin");

        given(personRepoMock.findByEmail(anyString())).willReturn(personMock);

        // When and Then
        mockMvc.perform(get("/dashboard").with(user("Mock Admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("username",equalTo("Mock Admin")))
                .andExpect(model().attribute("roles",equalTo("[ROLE_ADMIN]")))
                .andExpect(view().name("dashboard.html"))
                .andExpect(content().string(containsString("Welcome - Mock Admin")))
                .andExpect(content().string(containsString("Messages")))
                .andDo(print());
    }

}