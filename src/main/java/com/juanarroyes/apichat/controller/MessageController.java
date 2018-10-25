package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.ChatNotFoundException;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.Message;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.MessageRequest;
import com.juanarroyes.apichat.service.ChatServiceImpl;
import com.juanarroyes.apichat.service.MessageServiceImpl;
import com.juanarroyes.apichat.service.TokenService;
import com.juanarroyes.apichat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController{

    private MessageServiceImpl messageService;
    private ChatServiceImpl chatService;

    @Autowired
    public MessageController(MessageServiceImpl messageService, UserService userService, TokenService tokenService, ChatServiceImpl chatService) {
        super(tokenService, userService);
        this.messageService = messageService;
        this.chatService = chatService;
    }

    @GetMapping("/chat/{chat_id}")
    public ResponseEntity<List<Message>> getMessagesByChat(@PathVariable("chat_id") Long chatId) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<Message> messages = null;

        try {
            User user = getUserFromToken();

            Chat chat = chatService.getChatByIdAndUser(chatId, user);
            messages = messageService.getMessagesByChat(chat);
            httpStatus = HttpStatus.OK;
        } catch (ChatNotFoundException e) {
            log.error(e.getMessage(), e);
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            log.error("Unexpected error in method getMessagesByChat", e);
        }
        return new ResponseEntity<>(messages, httpStatus);
    }

    /**
     *
     * @param roomId
     * @return
     */
    @GetMapping("/room/{room_id}")
    public ResponseEntity<List<Message>> getRoomChatMessages(@PathVariable("room_id") Long roomId)
    {
        return null;
    }

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest request) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Message message = null;

        try {
            User user = getUserFromToken();
            Chat chat = chatService.getChatByIdAndUser(request.getChatId(), user);
            message = messageService.sendMessage(user, chat, request.getMessageText(), request.getMessageType(), request.getAttachUrl());
            httpStatus = HttpStatus.OK;
        } catch (ChatNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            log.error("Unexpected error in method sendMessage", e);
        }
        return new ResponseEntity<>(message, httpStatus);
    }

}
