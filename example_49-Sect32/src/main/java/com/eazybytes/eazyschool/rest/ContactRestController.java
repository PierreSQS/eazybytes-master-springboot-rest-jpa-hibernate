package com.eazybytes.eazyschool.rest;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.model.Response;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/contact", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
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
    public ResponseEntity<Response> saveMsg(@RequestHeader String invocationFrom, @Valid @RequestBody Contact contact) {
        log.info("####### Header invocationFrom: {} #######",invocationFrom);
        contactRepo.save(contact);

        Response response = new Response();
        response.setStatusCode(HttpStatus.CREATED.toString());
        response.setStatusMsg("Message saved successfully!");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMsgSaved","true")
                .body(response);
    }

    @DeleteMapping("deleteMsg")
    public ResponseEntity<Response> deleteMsg(RequestEntity<Contact> requestEntity) {
        // preparing the ResponseEntity
        Response response = new Response();
        HttpStatus httpStatus = HttpStatus.OK;

        log.info("####### Displaying the request Headers....#######");
        requestEntity.getHeaders().forEach((key, values) -> log.info("Header:{} = {}", key, values));

        // Getting the contact from the Request
        Contact contactToDelete = requestEntity.getBody();

        // Check whether the contact exists
        assert contactToDelete != null;
        Optional<Contact> foundIdToDeleteFromDBOpt = contactRepo.findById(contactToDelete.getContactId());

        if (foundIdToDeleteFromDBOpt.isPresent()) {
            Contact contactTodelete = foundIdToDeleteFromDBOpt.get();

            // Delete Contact if exists
            contactRepo.delete(contactTodelete);

            // Set the response and status
            setResponse(response, httpStatus.toString() ,"Message deleted successfully!!");
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            setResponse(response,httpStatus.toString(),
                    "Message with contactID: "+contactToDelete.getContactId()+" doesn't exists!");
        }

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    @PatchMapping("closeMsg")
    public ResponseEntity<Response> closeMsg(@RequestBody Contact contact) {
        // preparing the ResponseEntity
        Response response = new Response();
        HttpStatus httpStatus = HttpStatus.OK;

        // Check whether the contact exists
        Optional<Contact> foundIdToUpdateFromDBOpt = contactRepo.findById(contact.getContactId());

        if (foundIdToUpdateFromDBOpt.isPresent()) {
            Contact contactToUpdate = foundIdToUpdateFromDBOpt.get();

            // Delete Contact if exists
            contactToUpdate.setStatus(EazySchoolConstants.CLOSED);
            contactRepo.save(contactToUpdate);

            // Set the response and status
            setResponse(response, httpStatus.toString() ,"Message successfully updated!!");
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
            setResponse(response,httpStatus.toString(),
                    "Invalid contactID: "+contact.getContactId()+" received!");
        }

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    private void setResponse(Response response,String statusCode, String statusMsg) {
        response.setStatusCode(statusCode);
        response.setStatusMsg(statusMsg);

    }

}
