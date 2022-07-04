package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("student")
public class StudentController {

    @GetMapping("displayCourses")
    public ModelAndView displayStudentEnrolledCourses(HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView("courses_enrolled.html");
        Person studentWithCourses = (Person) httpSession.getAttribute("person");

        modelAndView.addObject("person",studentWithCourses);

        return modelAndView;
    }

}
