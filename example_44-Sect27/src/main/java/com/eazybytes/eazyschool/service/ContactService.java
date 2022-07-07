package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        return (savedContact.getContactId()>0);
    }

    public Page<Contact> findMsgsWithOpenStatus(int pageNum, String sortField, String sortDir){
        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNum-1, pageSize,
                sortDir.equals("asc")?Sort.by(sortField).ascending():Sort.by(sortField).descending());

        return contactRepository.findByStatus(EazySchoolConstants.OPEN, pageable);
    }

    public boolean updateContactStatus(int contactId){
        int updatedRows = contactRepository.updateMsgStatusByIdNative(EazySchoolConstants.CLOSED, contactId);

        return updatedRows > 0;

    }

}
