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
        boolean isSaved = false;
        contact.setStatus(EazySchoolConstants.OPEN);
        Contact savedContact = contactRepository.save(contact);
        if(null != savedContact && savedContact.getContactId()>0) {
            isSaved = true;
        }
        return isSaved;
    }

    public List<Contact> findMsgsWithOpenStatus(){
        return contactRepository.findByStatus(EazySchoolConstants.OPEN);
    }

    public boolean updateMsgStatus(int contactId){
        boolean isUpdated = false;
        Optional<Contact> contact = contactRepository.findById(contactId);
        contact.ifPresent(contact1 -> contact1.setStatus(EazySchoolConstants.CLOSE));
        Contact updatedContact = contactRepository.save(contact.get());
        if(null != updatedContact && updatedContact.getUpdatedBy()!=null) {
            isUpdated = true;
        }
        return isUpdated;
    }

}
