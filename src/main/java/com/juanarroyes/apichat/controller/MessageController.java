package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.model.Message;
import com.juanarroyes.apichat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

    private MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
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
