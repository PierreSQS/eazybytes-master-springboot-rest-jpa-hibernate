package com.eazybytes.eazyschool.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AdminController.class})
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(username = "Mock Admin",roles = {"ADMIN"})
    void displayClasses() throws Exception {
        mockMvc.perform(get("/admin/displayClasses"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("eazyClass"))
                .andExpect(view().name("classes.html"))
                .andExpect(content().string(containsString("EazySchool Class Details")))
                .andDo(print());
    }
}