package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/public")
public class PublicController {

    private final PersonService personSrv;

    public PublicController(PersonService personSrv) {
        this.personSrv = personSrv;
    }

    @GetMapping("/register")
    public String displayRegistrationPage(Model model) {
        model.addAttribute("person", new Person());
        return "register.html";
    }

    @PostMapping("/createUser")
    public String createUser(@Valid @ModelAttribute Person person, Errors errors) {
        if (errors.hasErrors()){
            return "register.html";
        }

        personSrv.saveUser(person);

        return "redirect:/login?register=true";
    }
}
