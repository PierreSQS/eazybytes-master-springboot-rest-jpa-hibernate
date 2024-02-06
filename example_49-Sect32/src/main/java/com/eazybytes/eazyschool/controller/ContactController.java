package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class ContactController {

    @Value("${eazyschool.contact.successMsg}")
    private String message;

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping("/contact")
    public String displayContactPage(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact.html";
    }

    @PostMapping("/saveMsg")
    public String saveMessage(@Valid @ModelAttribute Contact contact, Errors errors) {
        if(errors.hasErrors()){
            log.error("Contact form validation failed due to : " + errors.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
            return "contact.html";
        }
        boolean isContactSaved = contactService.saveMessageDetails(contact);
        if (isContactSaved) {
            log.info("######### 1 contact saved #########");
            log.info(String.format("### %s ###%n",message));
        }
        return "redirect:/contact";
    }

    @GetMapping("/displayMessages/page/{pageNum}")
    public ModelAndView displayMessages(@PathVariable int pageNum,
                                        @RequestParam String sortField, @RequestParam String sortDir) {
        Page<Contact> contactMsgPages = contactService.findMsgsWithOpenStatus(pageNum, sortField,sortDir);

        List<Contact> contactMsgList = contactMsgPages.getContent();

        ModelAndView modelAndView = new ModelAndView("messages.html");
        modelAndView.addObject("contactMsgs",contactMsgList);
        modelAndView.addObject("currentPage",pageNum);
        modelAndView.addObject("totalPages",contactMsgPages.getTotalPages());
        modelAndView.addObject("sortField",sortField);
        modelAndView.addObject("sortDir",sortDir);
        modelAndView.addObject("reverseSortDir",sortDir.equals("asc")? "desc": "asc");
        return modelAndView;
    }

    @GetMapping("/closeMsg")
    public String closeMsg(@RequestParam int id) {
        contactService.updateContactStatus(id);
        return "redirect:/displayMessages/page/1?sortField=name&sortDir=desc";
    }



}
