package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.config.ProjectSecurityConfig;
import com.eazybytes.eazyschool.model.Holiday;
import com.eazybytes.eazyschool.service.HolidayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HolidaysController.class)
@Import(value = {ProjectSecurityConfig.class})
class HolidaysControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    HolidayService holidaySrvMock;

    @BeforeEach
    void setUp() {
        // Given
        List<Holiday> holidaysMock = Arrays.asList(
                new Holiday(" Jan 1 ", "New Year's Day", Holiday.Type.FESTIVAL),
                new Holiday(" Oct 31 ", "Halloween", Holiday.Type.FESTIVAL),
                new Holiday(" Nov 24 ", "Thanksgiving Day", Holiday.Type.FESTIVAL),
                new Holiday(" Dec 25 ", "Christmas", Holiday.Type.FESTIVAL),
                new Holiday(" Jan 17 ", "Martin Luther King Jr. Day", Holiday.Type.FEDERAL),
                new Holiday(" July 4 ", "Independence Day", Holiday.Type.FEDERAL),
                new Holiday(" Sep 5 ", "Labor Day", Holiday.Type.FEDERAL),
                new Holiday(" Nov 11 ", "Veterans Day", Holiday.Type.FEDERAL)
        );

        given(holidaySrvMock.listHolidays()).willReturn(holidaysMock);

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
                .andExpect(content().string(containsString("Martin Luther King Jr. Day")))
                .andExpect(content().string(not(containsString("Thanksgiving Day"))))
                .andDo(print());
    }
}