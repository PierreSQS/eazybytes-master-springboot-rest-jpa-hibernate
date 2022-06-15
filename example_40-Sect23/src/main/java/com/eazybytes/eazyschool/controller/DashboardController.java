package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class DashboardController {

    private final PersonRepository personRepo;

    public DashboardController(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }


    @RequestMapping("/dashboard")
    public String displayDashboard(Model model, Authentication authentication, HttpSession httpSession) {

        Person foundPersByEmail = personRepo.findByEmail(authentication.getName());
        model.addAttribute("username", foundPersByEmail.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        httpSession.setAttribute("loggedUser",foundPersByEmail);
        return "dashboard.html";
    }

}
