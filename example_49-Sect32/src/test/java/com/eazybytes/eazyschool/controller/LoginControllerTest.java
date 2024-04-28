package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.config.EazySchoolProps;
import com.eazybytes.eazyschool.config.ProjectSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@Import({EazySchoolProps.class, ProjectSecurityConfig.class, H2ConsoleProperties.class})
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
    }

    @Test
    void displayLoginPageOK() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login.html"))
                .andExpect(content().string(containsString("<h3 class=\"title-style\">LogIn</h3>")))
                .andDo(print());
    }

    @Test
    void logoutPage() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login?logout=true"))
                .andDo(print());
    }
}