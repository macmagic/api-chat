package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.CreateChatRequest;
import com.juanarroyes.apichat.service.ChatServiceImpl;
import com.juanarroyes.apichat.service.TokenService;
import com.juanarroyes.apichat.service.UserService;
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
@RequestMapping(value = "/chat")
public class ChatController extends BaseController {

    private ChatServiceImpl chatService;

    @Autowired
    public ChatController(TokenService tokenService, UserService userService, ChatServiceImpl chatService) {
        super(tokenService, userService);
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<Chat> createPrivateChat(@Valid @RequestBody CreateChatRequest request) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Chat chat = null;

        try {
            User user = getUserFromToken();
            chat = chatService.createPrivateChat(user, request.getUserId());
            httpStatus = HttpStatus.CREATED;
        } catch(Exception e) {
            log.error("Unexpected error in method createPrivateChat", e);
        }
        return new ResponseEntity<>(chat, httpStatus);
    }


}
