package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contact")
    public String displayContactPage(Model model) {
        model.addAttribute("contact",new Contact());
        return "contact.html";
    }

    @PostMapping("/saveMsg")
    public ModelAndView saveMessage(@Valid Contact contact, Errors errors){

        if (errors.hasErrors()){
            log.error("Contact form Validation errors: {}",
                    errors.getFieldErrors().stream().map((DefaultMessageSourceResolvable::getDefaultMessage))
                            .collect(Collectors.toList()));
            return new ModelAndView("contact.html");
        }
        contactService.saveMessageDetails(contact);
        return new ModelAndView("redirect:/contact");
    }



}
