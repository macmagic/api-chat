package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.request.ContactRequest;
import com.juanarroyes.apichat.service.ContactListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import sun.net.www.http.HttpClient;

import javax.validation.Valid;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private ContactListServiceImpl contactListService;

    @Autowired
    public ContactController(ContactListServiceImpl contactListService) {
        this.contactListService = contactListService;
    }

    @PostMapping()
    public ResponseEntity<ContactList> createContactRelation(@Valid @RequestBody ContactRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ContactList contactList = null;

        try {

        } catch (HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        }

        return new ResponseEntity<>(contactList, httpStatus);
    }

}
