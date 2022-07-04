package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Course;
import com.eazybytes.eazyschool.model.Person;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    private Person personMock;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Course course1 = new Course();
        course1.setCourseId(1);
        course1.setName("Course1");

        Course course2 = new Course();
        course2.setCourseId(1);
        course2.setName("Course2");

        personMock = new Person();
        personMock.setPersonId(1);
        personMock.setCourses(Set.of(course1,course2));
    }

    @Test
    @WithMockUser(roles = {"STUDENT"})
    void displayStudentEnrolledCourses() throws Exception {

        mockMvc.perform(get("/student/displayCourses").sessionAttr("person",personMock))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("person"))
                .andExpect(view().name("courses_enrolled.html"))
                .andDo(print());
    }
}