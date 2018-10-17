package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.model.Message;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.MessageRepository;
import com.juanarroyes.apichat.service.MessageServiceImpl;
import com.juanarroyes.apichat.service.TokenService;
import com.juanarroyes.apichat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController extends BaseController{

    private MessageServiceImpl messageService;

    private UserService userService;

    @Autowired
    public MessageController(MessageServiceImpl messageService, UserService userService, TokenService tokenService) {
        super(tokenService, userService);
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<Message>> getUserChatMessages(@PathVariable("user_id") Long userId) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User user = getUserFromToken();
            User friend = userService.getUser(userId);
            messageService.getMessageByUserAndFriend(user, friend);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/room/{room_id}")
    public ResponseEntity<List<Message>> getRoomChatMessages(@PathVariable("room_id") Long roomId)
    {
        return null;
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        Message msgSended = null;

        try {

        } catch (Exception ex) {

        }

        return new ResponseEntity<>(msgSended, httpStatus);
    }

}
