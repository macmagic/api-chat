package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.helpers.TokenHelper;
import com.juanarroyes.apichat.model.Room;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.RoomRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

}