package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.ChatNotFoundException;
import com.juanarroyes.apichat.exception.UserNotAllowedException;
import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.helpers.TokenHelper;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.Room;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.RoomRequest;
import com.juanarroyes.apichat.request.UserRoomRequest;
import com.juanarroyes.apichat.request.UsersRoomRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {ChatRoomControllerTest.class}, secure = false)
@ComponentScan("com.juanarroyes.apichat")
@ActiveProfiles("test")
public class ChatRoomControllerTest extends AbstractControllerTest {

    @Before
    public void setUp() throws UserNotFoundException {
        //Mock for obtain user from token
        Mockito.when(userService.getUser(anyLong())).thenReturn(generateUser());
    }

    @Test
    public void testCreateRoom() throws Exception {
        RoomRequest roomRequest = new RoomRequest();
        List<Long> users = new ArrayList<>();
        users.add(100L);
        users.add(200L);
        users.add(300L);
        roomRequest.setRoomName("Test");
        roomRequest.setUsersRoom(users);
        Room room = DataHelper.getRandomRoom(100L);
        Mockito.when(roomService.createRoom(anyString(), anyString())).thenReturn(room);
        Mockito.when(chatService.createRoomChat(any(Room.class), any(User.class), any(List.class))).thenReturn(DataHelper.getChatRoom(room, 100L));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/chatroom")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(roomRequest));
        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    @Test
    public void testGetRooms() throws Exception {
        Mockito.when(chatService.getChatRoomsByUser(any(User.class))).thenReturn(DataHelper.getListOfChatRooms());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/chatroom")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testGetRoomById() throws Exception {
        Mockito.when(chatService.getChatById(anyLong())).thenReturn(DataHelper.getChatRoom(DataHelper.getRandomRoom()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/chatroom/1000")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testGetRoomByIdNotFound() throws Exception {
        Mockito.when(chatService.getChatById(anyLong())).thenThrow(new ChatNotFoundException()).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/chatroom/1000")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void testGetUsersFromRoom() throws Exception {
        User userParticipant = DataHelper.getRandomUser(1000L);
        Chat chatRoom = DataHelper.getChatRoom(DataHelper.getRandomRoom(1000L), 1000L);
        Mockito.when(chatService.getChatById(anyLong())).thenReturn(DataHelper.getChatRoom(DataHelper.getRandomRoom(1000L), 1000L));
        Mockito.when(chatParticipantService.getParticipantInChat(any(User.class), any(Chat.class))).thenReturn(DataHelper.getChatParticipant(chatRoom, userParticipant));
        Mockito.when(chatParticipantService.getListOfParticipantsInChat(any(Chat.class))).thenReturn(DataHelper.getListOfChatParticipant());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/chatroom/users/1000")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testAddUsersInRoom() throws Exception {
        UsersRoomRequest usersRoomRequest = new UsersRoomRequest();
        Chat chat = DataHelper.getChatRoom(DataHelper.getRandomRoom(1000L), 1000L);
        List<Long> users = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        usersRoomRequest.setChatRoomId(chat.getId());
        usersRoomRequest.setUsers(users);
        Mockito.when(chatService.getChatById(anyLong())).thenReturn(chat);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/chatroom/users")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(usersRoomRequest));
        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    @Test
    public void testDeleteRoom() throws Exception {
        Chat chat = DataHelper.getChatRoom(DataHelper.getRandomRoom(1000L), 1000L);
        Mockito.when(chatService.getChatById(anyLong())).thenReturn(chat);
        Mockito.when(chatParticipantService.isUserAdmin(any(Chat.class), any(User.class))).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/chatroom/id/1000")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testDeleteRoomUserNotAllowed() throws Exception {
        Chat chat = DataHelper.getChatRoom(DataHelper.getRandomRoom(1000L), 1000L);
        Mockito.when(chatService.getChatById(anyLong())).thenReturn(chat);
        Mockito.when(chatParticipantService.isUserAdmin(any(Chat.class), any(User.class))).thenReturn(false);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/chatroom/id/1000")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteUsersInRoom() throws Exception {
        UsersRoomRequest usersRoomRequest = new UsersRoomRequest();
        Chat chat = DataHelper.getChatRoom(DataHelper.getRandomRoom(1000L), 1000L);
        List<Long> users = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        usersRoomRequest.setChatRoomId(chat.getId());
        usersRoomRequest.setUsers(users);
        Mockito.when(chatService.getChatById(anyLong())).thenReturn(chat);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/chatroom/users")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usersRoomRequest));
        mockMvc.perform(requestBuilder).andExpect(status().isResetContent());
    }

    @Test
    public void testUpdateParticipantRol() throws Exception {
        UserRoomRequest userRoomRequest = new UserRoomRequest();
        Chat chat = DataHelper.getChatRoom(DataHelper.getRandomRoom(1000L), 1000L);
        userRoomRequest.setChatRoomId(chat.getId());
        userRoomRequest.setUserId(1000L);
        userRoomRequest.setAdmin(true);

        Mockito.when(chatService.getChatById(anyLong())).thenReturn(chat);
        Mockito.when(chatParticipantService.getParticipantInChat(any(User.class), any(Chat.class))).thenReturn(DataHelper.getChatParticipant(chat, DataHelper.getRandomUser(1000L), true));
        Mockito.when(chatParticipantService.updateParticipantRol(any(Chat.class), any(User.class), anyBoolean())).thenReturn(DataHelper.getChatParticipant(chat, DataHelper.getRandomUser(1200L), true));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/chatroom/changerol")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRoomRequest));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testLeaveARoom() throws Exception {
        Chat chat = DataHelper.getChatRoom(DataHelper.getRandomRoom(1000L), 1000L);
        Mockito.when(chatService.getChatById(anyLong())).thenReturn(chat);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/chatroom/leave/1000")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testKickUserFromRoom() throws Exception {
        UserRoomRequest userRoomRequest = new UserRoomRequest();
        Chat chat = DataHelper.getChatRoom(DataHelper.getRandomRoom(1000L), 1000L);
        userRoomRequest.setChatRoomId(chat.getId());
        userRoomRequest.setUserId(1000L);
        Mockito.when(chatService.getChatById(anyLong())).thenReturn(chat);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/chatroom/kick")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRoomRequest));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }
}