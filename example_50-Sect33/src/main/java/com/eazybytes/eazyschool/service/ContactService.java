package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.config.EazySchoolProps;
import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final EazySchoolProps eazySchoolProps;

    public ContactService(ContactRepository contactRepository, EazySchoolProps eazySchoolProps) {
        this.contactRepository = contactRepository;
        this.eazySchoolProps = eazySchoolProps;
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
        // There is a validation on the default page size in EazySchoolProps
        int pageSize = eazySchoolProps.getDefaultPageSize();
        Map<String, String> contactPropertiesMap = eazySchoolProps.getContact();

        if (contactPropertiesMap != null && contactPropertiesMap.get("pageSize") != null) {
            pageSize = Integer.parseInt(contactPropertiesMap.get("pageSize"));
            log.info("####### using the page size from the custom properties: {} #######",pageSize);
        } else  {
            log.info("####### using the default page size from the custom properties: {} #######",pageSize);
        }

        Pageable pageable = PageRequest.of(pageNum-1, pageSize,
                sortDir.equals("asc")?Sort.by(sortField).ascending():Sort.by(sortField).descending());

        return contactRepository.findByStatusWithQuery(EazySchoolConstants.OPEN, pageable);
    }

    public boolean updateContactStatus(int contactId){
        int updatedRows = contactRepository.updateMsgStatusByIdNative(EazySchoolConstants.CLOSED, contactId);

        return updatedRows > 0;

    }

}
