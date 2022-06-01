package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.config.WebConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = WebConfig.class)
class CourseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "Pierrot Mock",password = "pwd")
    void displayCoursePageWithMockUser() throws Exception{
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(content().string(containsString("<title>Eazy School - Best Educational Institute for your Child</title>")))
                .andDo(print());
    }

    @Test
    void displayCoursePageWithMockUser2() throws Exception{
        mockMvc.perform(get("/courses").with(user("mock user").password("12345")))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(content().string(containsString("<title>Eazy School - Best Educational Institute for your Child</title>")))
                .andDo(print());
    }

    @Test
    void displayCoursePageWithUserDetails() throws Exception{
        UserDetails user = User.withUsername("Pierrot User Details")
                .password("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
                .roles("USER")
                .build();

        mockMvc.perform(get("/courses").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(content().string(containsString("<title>Eazy School - Best Educational Institute for your Child</title>")))
                .andDo(print());
    }
}
