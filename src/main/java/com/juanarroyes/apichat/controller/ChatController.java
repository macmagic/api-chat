package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.service.TokenService;
import com.juanarroyes.apichat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/chat")
public class ChatController extends BaseController {

    @Autowired
    public ChatController(TokenService tokenService, UserService userService) {
        super(tokenService, userService);
    }

    public ResponseEntity<Chat> createChat() {
        return null;
    }


}
