package com.eazybytes.jobportal.contact.controller;

import org.springframework.web.bind.annotation.*;

import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.dto.ContactResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final IContactService contactService;

    @PostMapping(path = "/public", version = "1.0")
    public ResponseEntity<String> saveContactMsg(@RequestBody @Valid ContactRequestDto contactRequestDto) {
        boolean isSaved =  contactService.saveContact(contactRequestDto);
        if (isSaved) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Request processed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Request processing failed");
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgs() {
        List<ContactResponseDto> contactResponseDtos = contactService.fetchNewContactMsgs();
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtos);
    }

    @GetMapping("/sort/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgsWithSort(
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        List<ContactResponseDto> contactResponseDtos = contactService.fetchNewContactMsgsWithSort(sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtos);
    }

}
