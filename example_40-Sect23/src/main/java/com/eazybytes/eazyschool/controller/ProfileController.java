package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/displayProfile")
    public String displayProfilePage(Model model) {
        model.addAttribute("profile",new Profile());
        return "profile.html";
    }

}
