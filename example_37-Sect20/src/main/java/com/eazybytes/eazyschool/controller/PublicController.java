package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/register")
    public String displayRegistrationPage(Model model) {
        model.addAttribute("person", new Person());
        return "register.html";
    }
}
