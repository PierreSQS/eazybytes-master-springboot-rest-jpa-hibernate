package com.eazybytes.eazyschool.rest;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.model.Response;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("getMessagesByStatusWithBody")
    public List<Contact> getContactMessagesByStatusWithBody(@RequestBody Contact contact) {
        return contactRepo.findByStatus(contact.getStatus());
    }

    @PostMapping("saveMsg")
    public ResponseEntity<Response> saveMsg(@RequestHeader String invocationForm, @Valid @RequestBody Contact contact) {
        log.info("####### Header invocationForm: {}#######",invocationForm);
        contactRepo.save(contact);

        Response response = new Response();
        response.setStatusCode(HttpStatus.CREATED.toString());
        response.setStatusMsg("Message saved successfully!");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMsgSaved","true")
                .body(response);
    }

}
