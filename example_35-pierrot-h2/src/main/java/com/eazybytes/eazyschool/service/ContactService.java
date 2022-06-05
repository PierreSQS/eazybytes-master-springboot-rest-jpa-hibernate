package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
@Slf4j, is a Lombok-provided annotation that will automatically generate an SLF4J
Logger static property in the class at compilation time.
* */
@Slf4j
@Service
@ApplicationScope
public class ContactService {

    private final ContactRepository contactRepo;

    public ContactService(ContactRepository contactRepo){
        log.info("Contact Service Bean initialized");
        this.contactRepo = contactRepo;
    }

    /*
     * Save Contact Details into DB
     * @param the contact to submit
     * @return boolean
     */
    public boolean saveMessageDetails(Contact contact){
        contact.setStatus(EazySchoolConstants.OPEN);
        contact.setCreatedAt(LocalDateTime.now());
        contact.setCreatedBy(contact.getName());

        Contact savedContact = contactRepo.save(contact);
        log.info("the contact saved: {}", savedContact);
        return savedContact.getContactId() > 0;
    }

    public List<Contact> findContactMsgWithOpenStatus(String status) {
        return contactRepo.findByStatus(status);
    }

    public int updateContactStatus(Integer id, String updatedBy) {
        Optional<Contact> foundContactOpt = contactRepo.findById(id);
        if (foundContactOpt.isPresent()) {
            Contact foundContact = foundContactOpt.get();
            foundContact.setUpdatedBy(updatedBy);
            foundContact.setStatus(EazySchoolConstants.CLOSE);
            foundContact.setUpdatedAt(LocalDateTime.now());
            contactRepo.save(foundContact);
            return 1;
        }
        return 0;
    }
}
