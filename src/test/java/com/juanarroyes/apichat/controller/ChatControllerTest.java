package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.ChatNotFoundException;
import com.juanarroyes.apichat.exception.ContactListNotFoundException;
import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.helpers.TokenHelper;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.ChatRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {ChatController.class}, secure = false)
@ComponentScan("com.juanarroyes.apichat")
@ActiveProfiles("test")
public class ChatControllerTest extends AbstractControllerTest {

    public ChatControllerTest() {
        authRequired = true;
    }

    /*@Before
    public void setUp() throws UserNotFoundException {
        //Mock for obtain user from token
        Mockito.when(userService.getUser(anyLong())).thenReturn(generateUser());
    }*/

    @Test
    public void testCreatePrivateChat() throws Exception {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setUserId(2200L);

        Mockito.when(chatService.createPrivateChat(any(User.class), any(User.class))).thenReturn(DataHelper.getChatPrivate());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/chat")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .content(objectMapper.writeValueAsString(chatRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    @Test
    public void testGetChat() throws Exception {
        Mockito.when(chatService.getChatById(anyLong())).thenReturn(DataHelper.getChatPrivate());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/chat/"+2000)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testGetChatNotFound() throws Exception {
        Mockito.when(chatService.getChatById(anyLong())).thenThrow(new ChatNotFoundException()).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/chat/"+2000)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void testGetChatsByUser() throws Exception {
        List<Chat> listChats = new ArrayList<>();
        listChats.add(DataHelper.getChatPrivate());
        listChats.add(DataHelper.getChatPrivate());
        listChats.add(DataHelper.getChatPrivate());
        Mockito.when(chatService.getChatsByUser(any(User.class))).thenReturn(listChats);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/chat/"+2100)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }


}
