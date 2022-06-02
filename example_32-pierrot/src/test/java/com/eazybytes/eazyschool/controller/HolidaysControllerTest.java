package com.eazybytes.eazyschool.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HolidaysController.class)
class HolidaysControllerTest {
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void displayHolidaysAll() throws Exception{
        mockMvc.perform(get("/holidays/{display}","all"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("festival",true))
                .andExpect(model().attribute("federal",true))
                .andExpect(model().attribute("FESTIVAL",
                        hasItem(hasProperty("reason",equalTo("Halloween")))))
                .andExpect(view().name("holidays.html"))
                .andExpect(content().string(containsString("<h3 class=\"title-style\">Awesome Holidays</h3>")))
                .andDo(print());
    }

    @Test
    void displayHolidaysFederal() throws Exception{
        mockMvc.perform(get("/holidays/{display}","federal"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("federal",true))
                .andExpect(model().attributeExists("FEDERAL"))
                .andExpect(model().attributeDoesNotExist("festival"))
                .andExpect(model().attributeExists("FESTIVAL"))
                .andExpect(view().name("holidays.html"))
                .andExpect(content().string(containsString("<h3 class=\"title-style\">Awesome Holidays</h3>")))
                .andDo(print());
    }
}