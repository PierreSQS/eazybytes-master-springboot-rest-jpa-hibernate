package com.eazybytes.eazyschool.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void displayDashboardForMockUser() throws Exception {
        mockMvc.perform(get("/dashboard").with(user("Mock User")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("username",equalTo("Mock User")))
                .andExpect(model().attribute("roles",equalTo("[ROLE_USER]")))
                .andExpect(view().name("dashboard.html"))
                .andExpect(content().string(containsString("Welcome - Mock User")))
                .andDo(print());
    }
}