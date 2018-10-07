package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.ContactListAlreadyExistsException;
import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserRequest;
import com.juanarroyes.apichat.request.UserAnswerRequest;
import com.juanarroyes.apichat.request.UserRequestRequest;
import com.juanarroyes.apichat.service.*;
import com.juanarroyes.apichat.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/request")
public class RequestController {

    private UserRequestService userRequestService;
    private UserService userService;
    private ContactListService contactListService;
    private TokenService tokenService;

    @Autowired
    public RequestController(UserRequestService userRequestService, ContactListService contactListService, TokenService tokenService, UserService userService) {
        this.userRequestService = userRequestService;
        this.contactListService = contactListService;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserRequest> createRequest(@Valid @RequestBody UserRequestRequest userRequestRequest, HttpServletRequest request) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        UserRequest result = null;

        try {
            Long userSourceId = tokenService.getUserIdByToken(HttpUtils.getAccessTokenFromRequest(request));
            User userSource = userService.getUser(userSourceId);

            if (userSource == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }

            User userTarget = userService.getUser(userRequestRequest.getUserId());

            if (userTarget == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }

            try {
                result = userRequestService.createRequest(userTarget, userSource);
            } catch(ContactListAlreadyExistsException e) {
                log.error(e.getMessage());
                throw new HttpClientErrorException(HttpStatus.CONFLICT);
            }
            httpStatus = HttpStatus.CREATED;
        } catch (HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        } catch(Exception e) {
            log.error("Unexpected exception in method createRequest", e);
        }
        return new ResponseEntity<>(result, httpStatus);
    }

    @PutMapping
    public ResponseEntity answerRequest(@Valid @RequestBody UserAnswerRequest userAnswerRequest, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {

        } catch (Exception e) {

        }

        return new ResponseEntity(httpStatus);
    }

    @DeleteMapping
    public ResponseEntity deleteRequest(@Valid @RequestBody Long requestId) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {

        } catch (HttpClientErrorException e) {
            httpStatus = e.getStatusCode();
        }

        return new ResponseEntity(httpStatus);
    }


}
