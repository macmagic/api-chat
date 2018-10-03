package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.service.UserRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/request")
public class RequestController {

    private UserRequestService userRequestService;

    @Autowired
    public RequestController(UserRequestService userRequestService) {
        this.userRequestService = userRequestService;
    }




}
