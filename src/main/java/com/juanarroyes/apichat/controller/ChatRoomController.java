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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chatroom")
public class ChatRoomController extends BaseController{

    private RoomServiceImpl roomService;
    private ChatServiceImpl chatService;
    private ChatParticipantServiceImpl chatParticipantService;
    private UserService userService;

    @Autowired
    public ChatRoomController(TokenService tokenService, UserService userService, RoomServiceImpl roomService, ChatServiceImpl chatService, ChatParticipantServiceImpl chatParticipantService) {
        super(tokenService, userService);
        this.roomService = roomService;
        this.chatService = chatService;
        this.chatParticipantService = chatParticipantService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<Chat> createRoom(@RequestBody RoomRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Chat chat = null;

        try {
            User user = getUserFromToken();
            Room room = roomService.createRoom(request.getRoomName(), request.getRoomMessage());
            chat = chatService.createRoomChat(room, user, request.getUsersRoom());
            httpStatus = HttpStatus.CREATED;
        } catch(Exception ex) {
            log.error("Unexpected error in method createRoom", ex);
        }
        return new ResponseEntity<>(chat, httpStatus);
    }

    @GetMapping()
    public ResponseEntity<List<Chat>> getRooms() {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<Chat> chatRooms = new ArrayList<>();

        try {
            User user = getUserFromToken();
            chatRooms = chatService.getChatRoomsByUser(user);
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            log.error("Unexpected error in method getRooms", e);
        }

        return new ResponseEntity<>(chatRooms, httpStatus);
    }

    @GetMapping("/{chat_room_id}")
    public ResponseEntity<Chat> getRoomById(@PathVariable("chat_room_id") Long id) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Chat chat = null;

        try {
            chat = chatService.getChatById(id);
            httpStatus = HttpStatus.OK;
        } catch(ChatNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch(Exception e) {
            log.error("Unexpected error in method getRoomById", e);
        }
        return new ResponseEntity<>(chat, httpStatus);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<ChatParticipant>> getUsersFromRoom(@PathVariable("id") Long chatId) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<ChatParticipant> chatParticipants = new ArrayList<>();

        try {
            User user = getUserFromToken();
            Chat chat = chatService.getChatById(chatId);
            chatParticipantService.getParticipantInChat(user, chat);
            chatParticipants = chatParticipantService.getListOfParticipantsInChat(chat);
            httpStatus = HttpStatus.OK;
        } catch (ChatNotFoundException | ChatParticipantNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            log.error("Unexpected error in method getUsersFromRoom", e);
        }
        return new ResponseEntity<>(chatParticipants, httpStatus);
    }

    /* @TODO Return updated list from users in room chat */

    /**
     *
     * @param request
     * @return ResponseEntity with list of users and HTTP status code
     */
    @PostMapping("/users")
    public ResponseEntity addUsersInRoom(@Valid @RequestBody UsersRoomRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User user = getUserFromToken();
            Chat chat = chatService.getChatById(request.getChatRoomId());
            chatParticipantService.addParticipantsOnChat(chat, request.getUsers(), user);
            httpStatus = HttpStatus.CREATED;
        } catch (ChatParticipantNotFoundException | ChatNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            log.error("Unexpected error in method addUsersInRoom", e);
        }
        return new ResponseEntity(httpStatus);
    }

    @DeleteMapping("/id/{room_id}")
    public ResponseEntity deleteRoom(@PathVariable("room_id") Long chatRoomId) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User user = getUserFromToken();
            Chat chat = chatService.getChatById(chatRoomId);

            if(!chatParticipantService.isUserAdmin(chat, user)) {
                throw new UserNotAllowedException("User cannot allowed for this action");
            }
            chatService.deleteChatRoom(chat);
            httpStatus = HttpStatus.OK;
        } catch (ChatNotFoundException e) {
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
            Chat chat = chatService.getChatById(request.getChatRoomId());
            chatParticipantService.deleteParticipantsOnChat(chat, request.getUsers(), user);
            httpStatus = HttpStatus.RESET_CONTENT;
        } catch (ChatNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (UserNotAllowedException e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            log.error("Unexpected error in method deleteUsersInRoom", e);
        }
        return new ResponseEntity(httpStatus);
    }

    @PutMapping("/changerol")
    public ResponseEntity<ChatParticipant> updateParticipantRol(@Valid @RequestBody UserRoomRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ChatParticipant updatedParticipant = null;

        try {
            User user = getUserFromToken();
            User userUpdateRol = userService.getUser(request.getUserId());
            Chat chat = chatService.getChatById(request.getChatRoomId());
            ChatParticipant chatParticipant = chatParticipantService.getParticipantInChat(user, chat);
            if (!chatParticipant.isAdmin()) {
                throw new UserNotAllowedException("User is not admin");
            }
            updatedParticipant = chatParticipantService.updateParticipantRol(chat, userUpdateRol, request.getAdmin());
            httpStatus = HttpStatus.OK;
        } catch (ChatNotFoundException | UserNotFoundException e) {
            httpStatus = HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            log.error("Unexpected error in method updateParticipantRol", e);
        }
        return new ResponseEntity<>(updatedParticipant, httpStatus);
    }

    @DeleteMapping("/leave/{chat_room_id}")
    public ResponseEntity leaveARoom(@PathVariable("chat_room_id") Long chatRoomId) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            User user = getUserFromToken();
            Chat chat = chatService.getChatById(chatRoomId);
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
            Chat chat = chatService.getChatById(request.getChatRoomId());
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
