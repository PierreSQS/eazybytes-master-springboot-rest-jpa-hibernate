package com.eazybytes.jobportal.contact.service.impl;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.dto.ContactResponseDto;
import com.eazybytes.jobportal.entity.Contact;
import com.eazybytes.jobportal.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    @Override
    public boolean saveContact(ContactRequestDto contactRequestDto) {
        boolean result = false;
        Contact contact = contactRepository.save(transformToEntity(contactRequestDto));
        if(contact.getId() != null) {
            result = true;
        }
        return result;
    }

    @Override
    public List<ContactResponseDto> fetchNewContactMsgs() {
        List<Contact> contacts = contactRepository.findContactsByStatus(ApplicationConstants.NEW_MESSAGE);
        return contacts.stream().map(this::transformToDto).toList();
    }

    @Override
    public List<ContactResponseDto> fetchNewContactMsgsWithSort(String sortBy, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortBy);

        List<Contact> contacts = contactRepository.findContactsByStatus(ApplicationConstants.NEW_MESSAGE, sort);

        return contacts.stream().map(this::transformToDto).toList();
    }

    @Override
    public Page<ContactResponseDto> fetchContactMsgsWithPaginationAndSort(String status, int pageNumber, int pageSize,
                                                                          String sortBy, String sortDir) {
        String statusFilter = (status == null || status.isBlank()) ? ApplicationConstants.NEW_MESSAGE : status;
        // Create Sort object based on sortBy and sortDir parameters
        Sort sort = Sort.by(resolveDirection(sortDir), sortBy);
        // Create Pageable object with page number, page size, and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        // Fetch paginated and sorted contacts from repository
        return contactRepository.findContactsByStatus(statusFilter, pageable).map(this::transformToDto);
    }

    @Override
    @Transactional
    public boolean closeContactMsg(Long contactId) {
        Contact contact = contactRepository.findById(contactId).orElse(null);
        if (contact == null) {
            return false;
        }

        contact.setStatus(ApplicationConstants.CLOSED_MESSAGE);
        contactRepository.save(contact);
        return true;
    }

    private Contact transformToEntity(ContactRequestDto contactRequestDto) {
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactRequestDto, contact);
        contact.setStatus(ApplicationConstants.NEW_MESSAGE);
        return contact;
    }

    private ContactResponseDto transformToDto(Contact contact) {
        return new ContactResponseDto(contact.getId(),
                contact.getName(), contact.getEmail(), contact.getUserType(), contact.getSubject(),
                contact.getMessage(), contact.getStatus(), contact.getCreatedAt());
    }

    private Sort.Direction resolveDirection(String sortDir) {
        return "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
    }
}
