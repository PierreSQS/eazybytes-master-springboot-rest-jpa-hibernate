package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Contact;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String displayContactPage() {
        return "contact.html";
    }

    @PostMapping("saveMsg")
    public String submitContactMsg(Contact contact) {
        return "redirect:/contact";
    }
}
