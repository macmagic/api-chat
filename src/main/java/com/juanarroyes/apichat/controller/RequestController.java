package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.ContactListAlreadyExistsException;
import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.exception.UserRequestNotFoundException;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/request")
public class RequestController extends BaseController {

    private UserRequestService userRequestService;
    private UserService userService;
    private ContactListService contactListService;
    //private TokenService tokenService;

    @Autowired
    public RequestController(UserRequestService userRequestService, ContactListService contactListService, TokenService tokenService, UserService userService) {
        super(tokenService, userService);
        this.userRequestService = userRequestService;
        this.contactListService = contactListService;
        //this.tokenService = tokenService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserRequest>> getRequest(HttpServletRequest request) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<UserRequest> listRequest = null;

        try {
            User user = getUserFromToken();

            if(user == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }

            listRequest = userRequestService.getAllRequestByUser(user);
            httpStatus = HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            httpStatus = e.getStatusCode();
        } catch (Exception e) {
            log.error("Unexpected error in method getRequest", e);
        }
        return new ResponseEntity<>(listRequest, httpStatus);
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
    public ResponseEntity deleteRequest(@Valid @RequestBody Long requestId, HttpServletRequest request) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            UserRequest userRequest = userRequestService.getRequest(requestId);
            String token = HttpUtils.getAccessTokenFromRequest(request);
            User user = userService.getUser(tokenService.getUserIdByToken(token));

            if(!user.getId().equals(userRequest.getUser().getId())) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }
            userRequestService.deleteRequest(userRequest);
            httpStatus = HttpStatus.OK;
        } catch (UserNotFoundException | UserRequestNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (HttpClientErrorException e) {
            httpStatus = e.getStatusCode();
        } catch (Exception e) {
            log.error("Unexpected error in method deleteRequest", e);
        }
        return new ResponseEntity(httpStatus);
    }
}
