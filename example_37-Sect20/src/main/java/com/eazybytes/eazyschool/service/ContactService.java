package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
@Slf4j, is a Lombok-provided annotation that will automatically generate an SLF4J
Logger static property in the class at compilation time.
* */
@Slf4j
@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * Save Contact Details into DB
     * @param contact to submit
     * @return boolean
     */
    public boolean saveMessageDetails(Contact contact){
        contact.setStatus(EazySchoolConstants.OPEN);
        Contact savedContact = contactRepository.save(contact);
        return savedContact.getContactId()>0;
    }

    public List<Contact> findMsgsWithOpenStatus(){
        return contactRepository.findByStatus(EazySchoolConstants.OPEN);
    }

    public boolean updateContactStatus(int contactId){
        Optional<Contact> contactOpt = contactRepository.findById(contactId);

        if (contactOpt.isPresent()) {
            Contact foundContact = contactOpt.get();
            foundContact.setStatus(EazySchoolConstants.CLOSE);
            contactRepository.save(foundContact);
            return true;
        } else {
           return false;
        }
    }

}
