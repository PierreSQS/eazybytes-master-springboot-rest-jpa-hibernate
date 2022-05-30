package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Holiday;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HolidaysController.class)
class HolidaysControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void displayHolidays() throws Exception {
        mockMvc.perform(get("/holidays"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(Holiday.Type.FESTIVAL.toString(),
                        hasItem(hasProperty("reason",equalTo("Christmas")))))
                .andExpect(view().name("holidays.html"))
                .andExpect(content().string(containsString("<h2>Christmas</h2>")));
    }
}