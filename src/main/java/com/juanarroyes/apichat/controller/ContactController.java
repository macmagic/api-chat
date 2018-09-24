package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.ContactAnswerRequest;
import com.juanarroyes.apichat.request.ContactRequest;
import com.juanarroyes.apichat.security.UserPrincipal;
import com.juanarroyes.apichat.service.ContactListServiceImpl;
import com.juanarroyes.apichat.service.TokenService;
import com.juanarroyes.apichat.service.UserService;
import com.juanarroyes.apichat.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import sun.net.www.http.HttpClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/contact")
public class ContactController {

    private ContactListServiceImpl contactListService;

    private UserService userService;

    private TokenService tokenService;

    @Autowired
    public ContactController(ContactListServiceImpl contactListService, UserService userService, TokenService tokenService) {
        this.contactListService = contactListService;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/request")
    public ResponseEntity contactRequest(@Valid @RequestBody ContactRequest bodyRequest, HttpServletRequest request) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            String token = HttpUtils.getAccessTokenFromRequest(request);
            Long userId = tokenService.getUserIdByToken(token);
            User user = userService.getUser(userId);
            User userRequest = userService.getUser(bodyRequest.getUserRequestId());
            boolean result = contactListService.createRequest(user, userRequest);
            httpStatus = (result) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        } catch (UserNotFoundException ex) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception ex) {
            log.error("Unexpected error in method contactRequest", ex);
        }

        return new ResponseEntity(httpStatus);
    }

    @PutMapping("/request")
    public ResponseEntity answerRequest(@Valid @RequestBody ContactAnswerRequest body, HttpServletRequest request) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            String token = HttpUtils.getAccessTokenFromRequest(request);
            Long userId = tokenService.getUserIdByToken(token);
            User userOwner = userService.getUser(userId);
            contactListService.answerRequest(userOwner, body.getUserRequestResponse());

        } catch (UserNotFoundException ex) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception ex) {

        }

        return new ResponseEntity(httpStatus);
    }


    /*@PostMapping()
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
    }*/

}
