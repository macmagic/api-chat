package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.UserNotAllowedException;
import com.juanarroyes.apichat.model.ChatParticipant;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.ChatParticipantRequest;
import com.juanarroyes.apichat.service.ChatParticipantServiceImpl;
import com.juanarroyes.apichat.service.TokenService;
import com.juanarroyes.apichat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/chatparticipant")
public class ChatParticipantController extends BaseController {

    private ChatParticipantServiceImpl chatParticipantService;

    @Autowired
    public ChatParticipantController(TokenService tokenService, UserService userService, ChatParticipantServiceImpl chatParticipantService) {
        super(tokenService, userService);
        this.chatParticipantService = chatParticipantService;
    }

    @PostMapping
    public ResponseEntity addParticipant(@Valid @RequestBody ChatParticipantRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User user = getUserFromToken();
            ChatParticipant chatParticipant = chatParticipantService.getParticipantInChat(user, new Long(1));
        } catch(Exception e) {

        }

        return new ResponseEntity(httpStatus);

    }


}
