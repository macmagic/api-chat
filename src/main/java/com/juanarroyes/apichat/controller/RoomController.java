package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.ChatNotFoundException;
import com.juanarroyes.apichat.exception.ChatParticipantNotFoundException;
import com.juanarroyes.apichat.exception.RoomNotFoundException;
import com.juanarroyes.apichat.exception.UserNotAllowedException;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.Room;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.UsersRoomRequest;
import com.juanarroyes.apichat.request.CreateRoomRequest;
import com.juanarroyes.apichat.response.RoomCreationResponse;
import com.juanarroyes.apichat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/room")
public class RoomController extends BaseController{

    private RoomServiceImpl roomService;
    private ChatServiceImpl chatService;
    private ChatParticipantServiceImpl chatParticipantService;

    @Autowired
    public RoomController(TokenService tokenService, UserService userService, RoomServiceImpl roomService, ChatServiceImpl chatService, ChatParticipantServiceImpl chatParticipantService) {
        super(tokenService, userService);
        this.roomService = roomService;
        this.chatService = chatService;
        this.chatParticipantService = chatParticipantService;
    }

    @PostMapping()
    public ResponseEntity<RoomCreationResponse> createRoom(@RequestBody CreateRoomRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        RoomCreationResponse response = null;
        try {
            Room room = roomService.createRoom(request.getRoomName());
            Chat chat = chatService.createRoomChat(room, request.getUsersRoom());
            response = new RoomCreationResponse(room, chat);
            httpStatus = HttpStatus.CREATED;
        } catch(Exception ex) {
            log.error("Unexpected error in method createRoom", ex);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @PostMapping("/users")
    public ResponseEntity addUsersInRoom(@Valid @RequestBody UsersRoomRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User user = getUserFromToken();
            Room room = roomService.getRoomById(request.getRoomId());
            Chat chat = chatService.getChatByRoom(room);
            chatParticipantService.addParticipantsOnChat(chat, request.getUsers(), user);
            httpStatus = HttpStatus.CREATED;
        } catch (RoomNotFoundException | ChatNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            log.error("Unexpected error in method addUsersInRoom", e);
        }
        return new ResponseEntity(httpStatus);
    }

    @DeleteMapping("/users")
    public ResponseEntity deleteUsersInRoom(@Valid @RequestBody UsersRoomRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User user = getUserFromToken();
            Room room = roomService.getRoomById(request.getRoomId());
            Chat chat = chatService.getChatByRoom(room);
            chatParticipantService.deleteParticipantsOnChat(chat, request.getUsers(), user);
            httpStatus = HttpStatus.RESET_CONTENT;
        } catch (RoomNotFoundException | ChatParticipantNotFoundException | ChatNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (UserNotAllowedException e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            log.error("Unexpected error in method deleteUsersInRoom", e);
        }
        return new ResponseEntity(httpStatus);
    }

    @GetMapping("/id/{room_id}")
    public ResponseEntity<Room> getRoomById(@PathVariable("room_id") Long id) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Room room = null;

        try {
            try {
                room = roomService.getRoomById(id);
            } catch(RoomNotFoundException ex) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
            httpStatus = HttpStatus.OK;
        } catch(HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        } catch(Exception ex) {
            log.error("Unexpected error in method getRoomById", ex);
        }
        return new ResponseEntity<>(room, httpStatus);
    }

    @DeleteMapping("/leave")
    public ResponseEntity leaveARoom(@RequestParam("room_id") Long roomId) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User user = getUserFromToken();
            Room room = roomService.getRoomById(roomId);
            Chat chat = chatService.getChatByRoom(room);
            chatParticipantService.leaveUserFromChat(chat, user);
            httpStatus = HttpStatus.OK;
        } catch(Exception e) {
            log.error("Unexpected error in method leaveARoom", e);
        }
        return new ResponseEntity(httpStatus);
    }
}
