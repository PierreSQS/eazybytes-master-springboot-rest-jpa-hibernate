package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.config.WebConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
                .andDo(print());
    }

    @Test
    void displayCoursePageWithRealUser() throws Exception{
        mockMvc.perform(get("/courses").with(user("eazybytes").password("12345")))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andDo(print());
    }
}
