package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.*;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.ChatParticipant;
import com.juanarroyes.apichat.model.Room;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.*;
import com.juanarroyes.apichat.response.RoomCreationResponse;
import com.juanarroyes.apichat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/room")
public class RoomController extends BaseController{

    private RoomServiceImpl roomService;
    private ChatServiceImpl chatService;
    private ChatParticipantServiceImpl chatParticipantService;
    private UserService userService;

    @Autowired
    public RoomController(TokenService tokenService, UserService userService, RoomServiceImpl roomService, ChatServiceImpl chatService, ChatParticipantServiceImpl chatParticipantService) {
        super(tokenService, userService);
        this.roomService = roomService;
        this.chatService = chatService;
        this.chatParticipantService = chatParticipantService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<RoomCreationResponse> createRoom(@RequestBody RoomRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        RoomCreationResponse response = null;
        try {
            User user = getUserFromToken();
            Room room = roomService.createRoom(request.getRoomName());
            Chat chat = chatService.createRoomChat(room, user, request.getUsersRoom());
            response = new RoomCreationResponse(room, chat);
            httpStatus = HttpStatus.CREATED;
        } catch(Exception ex) {
            log.error("Unexpected error in method createRoom", ex);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping()
    public ResponseEntity<List<Room>> getRooms() {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<Room> rooms = null;

        try {
            User user = getUserFromToken();
            rooms = roomService.getRoomsByUser(user);
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            log.error("Unexpected error in method getRooms", e);
        }

        return new ResponseEntity<>(rooms, httpStatus);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<ChatParticipant>> getUsersFromRoom(@PathVariable("id") Long roomId) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<ChatParticipant> chatParticipants = null;

        try {
            User user = getUserFromToken();
            Room room = roomService.getRoomById(roomId);
            Chat chat = chatService.getChatByRoom(room);
            chatParticipantService.getParticipantInChat(user, chat);
            chatParticipants = chatParticipantService.getListOfParticipantsInChat(chat);
            httpStatus = HttpStatus.OK;
        } catch (RoomNotFoundException | ChatNotFoundException | ChatParticipantNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            log.error("Unexpected error in method getUsersFromRoom", e);
        }
        return new ResponseEntity<>(chatParticipants, httpStatus);
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
        } catch (RoomNotFoundException | ChatParticipantNotFoundException | ChatNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            log.error("Unexpected error in method addUsersInRoom", e);
        }
        return new ResponseEntity(httpStatus);
    }

    @DeleteMapping("/id/{room_id}")
    public ResponseEntity deleteRoom(@PathVariable("room_id") Long roomId) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            Room room = roomService.getRoomById(roomId);
            User user = getUserFromToken();
            Chat chat = chatService.getChatByRoom(room);

            if(!chatParticipantService.isUserAdmin(chat, user)) {
                throw new UserNotAllowedException("User cannot allowed for this action");
            }
            roomService.deleteRoom(room);
            httpStatus = HttpStatus.OK;
        } catch (RoomNotFoundException | ChatNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (UserNotAllowedException e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            log.error("Unexpected error in method deleteRoom", e);
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
        } catch (RoomNotFoundException | ChatNotFoundException e) {
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
            room = roomService.getRoomById(id);
            httpStatus = HttpStatus.OK;
        } catch(RoomNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch(Exception e) {
            log.error("Unexpected error in method getRoomById", e);
        }
        return new ResponseEntity<>(room, httpStatus);
    }

    @PutMapping("/changerol")
    public ResponseEntity<ChatParticipant> updateParticipantRol(@Valid @RequestBody UserRoomRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ChatParticipant updatedParticipant = null;

        try {
            User user = getUserFromToken();
            User userUpdateRol = userService.getUser(request.getUserId());
            Room room = roomService.getRoomById(request.getRoomId());
            Chat chat = chatService.getChatByRoom(room);
            ChatParticipant chatParticipant = chatParticipantService.getParticipantInChat(user, chat);
            if (!chatParticipant.isAdmin()) {
                throw new UserNotAllowedException("User is not admin");
            }
            updatedParticipant = chatParticipantService.updateParticipantRol(chat, userUpdateRol, request.getAdmin());
            httpStatus = HttpStatus.OK;
        } catch (RoomNotFoundException | UserNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            log.error("Unexpected error in method updateParticipantRol", e);
        }
        return new ResponseEntity<>(updatedParticipant, httpStatus);
    }

    @DeleteMapping("/leave/{room_id}")
    public ResponseEntity leaveARoom(@PathVariable("room_id") Long roomId) {
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

    @DeleteMapping("/kick")
    public ResponseEntity kickUserFromRoom(@RequestBody UserRoomRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User user = getUserFromToken();
            User userKick = userService.getUser(request.getUserId());
            Room room = roomService.getRoomById(request.getRoomId());
            Chat chat = chatService.getChatByRoom(room);
            chatParticipantService.kickUserFromChat(chat, userKick, user);
            httpStatus = HttpStatus.OK;
        } catch (UserNotFoundException | ChatNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (UserNotAllowedException e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            log.error("Unexpected error in method kickUserFromRoom", e);
        }
        return new ResponseEntity(httpStatus);
    }
}
