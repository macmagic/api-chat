package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.ContactListNotFoundException;
import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.UserAnswerRequest;
import com.juanarroyes.apichat.request.ContactRequest;
import com.juanarroyes.apichat.service.ContactListServiceImpl;
import com.juanarroyes.apichat.service.TokenService;
import com.juanarroyes.apichat.service.UserService;
import com.juanarroyes.apichat.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/contact")
public class ContactController extends BaseController{

    private ContactListServiceImpl contactListService;

    private UserService userService;

    @Autowired
    public ContactController(ContactListServiceImpl contactListService, UserService userService, TokenService tokenService) {
        super(tokenService, userService);
        this.contactListService = contactListService;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<List<ContactList>> getContacts() {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<ContactList> contactList = null;

        try {
            User user = getUserFromToken();
            contactList = contactListService.getContactsByUserId(user.getId());

            if (contactList.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
            httpStatus = HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            httpStatus = e.getStatusCode();
        } catch (Exception e) {
            log.error("Unexpected error in method getContacts", e);
        }
        return new ResponseEntity<>(contactList, httpStatus);
    }

    @GetMapping("/{contact_id}")
    public ResponseEntity<ContactList> getContact(@PathVariable("contact_id") Long contactId) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ContactList contactList = null;

        try {
            User user = getUserFromToken();
            contactList = contactListService.getContactListByIdAndOwnerId(contactId, user.getId());

            if(contactList == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
            httpStatus = HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            httpStatus = e.getStatusCode();
        } catch (Exception e) {
            log.error("Unexpected error in method getContact", e);
        }
        return new ResponseEntity<>(contactList, httpStatus);
    }

    @PutMapping("/block")
    public ResponseEntity<ContactList> blockContact(@Valid @RequestBody Long contactId)
    {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ContactList contactList = null;

        try {
            User user = getUserFromToken();
            contactList = contactListService.blockContact(contactId, user.getId());
            httpStatus = HttpStatus.OK;
        } catch (ContactListNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (HttpClientErrorException e) {
            httpStatus = e.getStatusCode();
        } catch (Exception e) {
            log.error("Unexpected error in method blockContact", e);
        }
        return new ResponseEntity<>(contactList, httpStatus);
    }

    @PutMapping("/unblock")
    public ResponseEntity<ContactList> unblockContact(@Valid @RequestBody Long contactId) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ContactList contactList = null;

        try {
            User user = getUserFromToken();
            contactList = contactListService.unblockContact(contactId, user.getId());
        } catch (ContactListNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (HttpClientErrorException e) {
            httpStatus = e.getStatusCode();
        } catch (Exception e) {
            log.error("Unexpected error in method unblockContact", e);
        }
        return new ResponseEntity<>(contactList, httpStatus);
    }

    @DeleteMapping("/{contact_id}")
    public ResponseEntity deleteContact(@PathVariable("contact_id") Long contactId) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            if (contactId == null) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }
            User user = getUserFromToken();
            ContactList contactList = contactListService.getContactListByIdAndOwnerId(contactId, user.getId());

            if (contactList == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
            contactListService.deleteRelationById(contactList.getContactId());
            httpStatus = HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            httpStatus = e.getStatusCode();
        } catch (Exception e) {
            log.error("Unexpected error in method deleteContact", e);
        }
        return new ResponseEntity(httpStatus);
    }
}
