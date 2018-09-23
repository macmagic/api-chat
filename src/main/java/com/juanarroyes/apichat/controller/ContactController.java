package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.ContactRequest;
import com.juanarroyes.apichat.service.ContactListServiceImpl;
import com.juanarroyes.apichat.service.UserService;
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

    private UserService userService;

    @Autowired
    public ContactController(ContactListServiceImpl contactListService, UserService userService) {
        this.contactListService = contactListService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<ContactList> createContactRelation(@Valid @RequestBody ContactRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ContactList contactList = null;

        try {
            User userRequest = null;
            User userRequired = null;
            try {
                userRequest = userService.getUser(request.getUserRequestId());
                userRequired = userService.getUser(request.getUserRequiredId());
            } catch(UserNotFoundException ex) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }

            contactListService.createRelationship(userRequest, userRequired);
        } catch (HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        }

        return new ResponseEntity<>(contactList, httpStatus);
    }

}
