package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.*;
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
    private UserService userService;

    @Autowired
    public ChatController(TokenService tokenService, UserService userService, ChatServiceImpl chatService) {
        super(tokenService, userService);
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Chat> createPrivateChat(@Valid @RequestBody CreateChatRequest request) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Chat chat = null;

        try {
            User user = getUserFromToken();
            User userFriend = userService.getUser(request.getUserId());

            if (user.getId().equals(request.getUserId())) {
                throw new ChatUserIsTheSameException("User and user friend its the same");
            }

            try {
                chat = chatService.createPrivateChat(user, userFriend);
                httpStatus = HttpStatus.CREATED;
            } catch (ChatAlreadyExistsException e) {
                chat = chatService.getPrivateChatByUserAndFriend(user, userFriend);
                httpStatus = HttpStatus.OK;
            }

        } catch (ChatUserIsTheSameException e) {
            log.error(e.getMessage());
            httpStatus = HttpStatus.CONFLICT;
        } catch (UserNotFoundException | ContactListNotFoundException | ChatNotFoundException e) {
            log.info(e.getMessage());
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            log.error("Unexpected error in method createPrivateChat", e);
        }

        return new ResponseEntity<>(chat, httpStatus);
    }
}
