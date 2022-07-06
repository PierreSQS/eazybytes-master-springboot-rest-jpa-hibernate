package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class ContactController {

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
        if (isContactSaved) log.info("#### 1 contact saved #########");
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
        return modelAndView;
    }

    @GetMapping("/closeMsg")
    public String closeMsg(@RequestParam int id) {
        contactService.updateContactStatus(id);
        return "redirect:/displayMessages";
    }



}
