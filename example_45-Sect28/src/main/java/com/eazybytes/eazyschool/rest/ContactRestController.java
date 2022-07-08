package com.eazybytes.eazyschool.rest;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/contact")
public class ContactRestController {
    private final ContactRepository contactRepo;

    public ContactRestController(ContactRepository contactRepo) {
        this.contactRepo = contactRepo;
    }

    @GetMapping("getMessagesByStatus")
    public List<Contact> getContactMessagesByStatus(@RequestParam String status) {
        return contactRepo.findByStatus(status);
    }
}
