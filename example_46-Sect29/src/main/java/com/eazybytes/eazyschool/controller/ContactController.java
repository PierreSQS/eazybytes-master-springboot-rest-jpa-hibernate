package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.proxy.ContactProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContactController {

    private final ContactProxy contactProxy;

    public ContactController(ContactProxy contactProxy) {
        this.contactProxy = contactProxy;
    }

    @GetMapping("/getMessages")
    public List<Contact> getMessages(@RequestParam String status) {
        return contactProxy.getMessagesByStatus(status);
    }

}
