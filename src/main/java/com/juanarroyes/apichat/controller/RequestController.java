package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.request.UserRequestRequest;
import com.juanarroyes.apichat.service.UserRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/request")
public class RequestController {

    private UserRequestService userRequestService;

    @Autowired
    public RequestController(UserRequestService userRequestService) {
        this.userRequestService = userRequestService;
    }

    @PostMapping()
    public ResponseEntity createRequest(@Valid @RequestBody UserRequestRequest userRequestRequest) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {

        } catch(Exception e) {
            log.error("Unexpected exception in method createRequest", e);
        }

        return new ResponseEntity(httpStatus);
    }


}
