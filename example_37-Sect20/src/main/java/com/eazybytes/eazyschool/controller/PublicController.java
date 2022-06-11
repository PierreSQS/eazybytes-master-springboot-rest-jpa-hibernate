package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/public")
public class PublicController {

    private static final String REG_PAGE="register.html";

    private final PersonService personSrv;


    public PublicController(PersonService personSrv) {
        this.personSrv = personSrv;
    }

    @GetMapping("/register")
    public String displayRegistrationPage(Model model) {
        model.addAttribute("person", new Person());
        return REG_PAGE;
    }

    @PostMapping("/createUser")
    public String createUser(@Valid @ModelAttribute Person person, Errors errors) {
        if (errors.hasErrors()){
            return REG_PAGE;
        }

        Person savedPerson = personSrv.saveUser(person);

        if (savedPerson != null && savedPerson.getPersonId() > 0) {
            return "redirect:/login?register=true";
        } else {
           return REG_PAGE;
        }

    }
}
