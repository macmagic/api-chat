package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.ContactListAlreadyExistsException;
import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.exception.UserRequestNotFoundException;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserRequest;
import com.juanarroyes.apichat.request.UserAnswerRequest;
import com.juanarroyes.apichat.request.UserRequestRequest;
import com.juanarroyes.apichat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public RequestController(UserRequestService userRequestService, TokenService tokenService, UserService userService) {
        super(tokenService, userService);
        this.userRequestService = userRequestService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserRequest>> getRequest() {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<UserRequest> listRequest = null;

        try {
            User user = getUserFromToken();
            listRequest = userRequestService.getAllRequestByUser(user);

            if(listRequest.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
            httpStatus = HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            httpStatus = e.getStatusCode();
        } catch (Exception e) {
            log.error("Unexpected error in method getRequest", e);
        }
        return new ResponseEntity<>(listRequest, httpStatus);
    }

    @PostMapping
    public ResponseEntity<UserRequest> createRequest(@Valid @RequestBody UserRequestRequest userRequestRequest) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        UserRequest result = null;

        try {
            User userSource = getUserFromToken();

            if (userSource == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }

            User userTarget = userService.getUser(userRequestRequest.getUserId());
            result = userRequestService.createRequest(userTarget, userSource);
            httpStatus = HttpStatus.CREATED;
        } catch (UserNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (ContactListAlreadyExistsException e) {
            log.error("Conflict detected" + e.getMessage(), e);
            httpStatus  = HttpStatus.CONFLICT;
        } catch (HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        } catch(Exception e) {
            log.error("Unexpected exception in method createRequest", e);
        }
        return new ResponseEntity<>(result, httpStatus);
    }

    @PutMapping
    public ResponseEntity answerRequest(@Valid @RequestBody UserAnswerRequest userAnswerRequest) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User user = getUserFromToken();
            userRequestService.answerRequest(userAnswerRequest.getUserRequestResponse(), userAnswerRequest.getRequestId(), user);
            httpStatus = HttpStatus.OK;
        } catch (UserRequestNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            log.error("Unexpected error in method answerRequest", e);
        }
        return new ResponseEntity(httpStatus);
    }

    @DeleteMapping("/{request_id}")
    public ResponseEntity deleteRequest(@PathVariable("request_id") Long requestId) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User user = getUserFromToken();
            UserRequest userRequest = userRequestService.getRequest(requestId);

            if(!user.getId().equals(userRequest.getUser().getId())) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }
            userRequestService.deleteRequest(userRequest);
            httpStatus = HttpStatus.OK;
        } catch (UserRequestNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (HttpClientErrorException e) {
            httpStatus = e.getStatusCode();
        } catch (Exception e) {
            log.error("Unexpected error in method deleteRequest", e);
        }
        return new ResponseEntity(httpStatus);
    }
}
