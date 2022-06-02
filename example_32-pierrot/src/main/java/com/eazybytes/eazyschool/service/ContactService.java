package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.LocalDateTime;

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

        int savedContactCnt = contactRepo.saveContactMsg(contact);
        log.info("the contact saved: {}", contact);
        return (savedContactCnt > 0);
    }

}
